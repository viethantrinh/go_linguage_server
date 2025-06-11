package tech.trvihnls.features.main.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.*;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.*;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.*;
import tech.trvihnls.features.conversation.dtos.response.ConversationResponse;
import tech.trvihnls.features.level.dtos.response.LevelResponse;
import tech.trvihnls.features.main.dtos.response.*;
import tech.trvihnls.features.main.services.MainService;
import tech.trvihnls.features.topic.dtos.response.TopicResponse;
import tech.trvihnls.features.topic.services.TopicService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {
    private final UserRepository userRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserLessonAttemptRepository userLessonAttemptRepository;
    private final LevelRepository levelRepository;
    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final ExerciseRepository exerciseRepository;
    private final ConversationRepository conversationRepository;
    private final TopicService topicService;


    /**
     * Retrieves the home screen data for the current authenticated user.
     * This includes:
     * <ul>
     *     <li>User streak and go points</li>
     *     <li>User subscription status</li>
     *     <li>Available levels with topics and accumulated XP points</li>
     * </ul>
     *
     * @return A structured HomeResponse containing all required home screen data
     * @throws ResourceNotFoundException if the current user does not exist
     */
    @Override
    public HomeResponse retrieveHomeData() {
        // Get and validate current user
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new ResourceNotFoundException(ErrorCodeEnum.USER_NOT_EXISTED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.USER_NOT_EXISTED));

        // Check if user has an active subscription
        boolean isUserSubscribed = userSubscriptionRepository
                .existsByIsActiveAndPaymentStatusAndUserId(true, PaymentStatusEnum.SUCCEEDED.getValue(), user.getId());
        log.info("isUserSubscribed: {}", isUserSubscribed);

        // Build a map of lesson IDs to earned XP points
        Map<Long, Integer> lessonXPMap = buildLessonXPMap(user.getId());

        // Process levels, topics, and lessons data
        List<LevelResponse> levelResponses = processLevelsData(lessonXPMap, isUserSubscribed);

        // Build the final response
        return HomeResponse.builder()
                .streakPoints(user.getTotalStreakPoints())
                .goPoints(user.getTotalGoPoints())
                .isSubscribed(isUserSubscribed)
                .levels(levelResponses)
                .build();
    }

    /**
     * Builds a map of lesson IDs to XP points earned by the user.
     *
     * @param userId The ID of the user
     * @return A map where keys are lesson IDs and values are earned XP points
     */
    private Map<Long, Integer> buildLessonXPMap(Long userId) {
        List<UserLessonAttempt> lessonsByUser = userLessonAttemptRepository.findByUserId(userId);

        return lessonsByUser.stream()
                .collect(Collectors.toMap(
                        attempt -> attempt.getId().getLessonId(),
                        UserLessonAttempt::getXpPointsEarned,
                        (existing, replacement) -> existing // In case of duplicate keys, keep the existing value
                ));
    }

    /**
     * Processes all levels data, including their topics and lessons.
     *
     * @param lessonXPMap      Map of lesson IDs to XP points
     * @param isUserSubscribed Whether the user has an active subscription
     * @return List of LevelResponse objects with topic information
     */
    private List<LevelResponse> processLevelsData(Map<Long, Integer> lessonXPMap, boolean isUserSubscribed) {
        List<Level> levels = levelRepository.findAll(Sort.by("displayOrder").ascending());
        List<LevelResponse> levelResponses = new ArrayList<>();

        for (Level level : levels) {
            List<Topic> topics = topicRepository.findByLevelIdOrderByDisplayOrderAsc(level.getId());
            List<TopicResponse> topicResponses = new ArrayList<>();
            int levelTotalXp = 0;

            for (Topic topic : topics) {
                // Process topic data and calculate XP
                TopicResponse topicResponse = processTopicData(topic, lessonXPMap, isUserSubscribed);
                levelTotalXp += topicResponse.getTotalUserXPPoints();
                topicResponses.add(topicResponse);
            }

            // Build level response with its topics
            LevelResponse levelResponse = LevelResponse.builder()
                    .id(level.getId())
                    .name(level.getName())
                    .displayOrder(level.getDisplayOrder())
                    .totalUserXPPoints(levelTotalXp)
                    .topics(topicResponses)
                    .build();

            levelResponses.add(levelResponse);
        }

        return levelResponses;
    }

    /**
     * Processes a single topic's data, calculating total XP and premium status.
     *
     * @param topic            The topic entity to process
     * @param lessonXPMap      Map of lesson IDs to XP points
     * @param isUserSubscribed Whether the user has an active subscription
     * @return A TopicResponse containing the processed topic data
     */
    private TopicResponse processTopicData(Topic topic, Map<Long, Integer> lessonXPMap, boolean isUserSubscribed) {
        List<Lesson> lessons = lessonRepository.findByTopicIdOrderByDisplayOrderAsc(topic.getId());

        // Calculate total XP for this topic from all its lessons
        int topicTotalXp = lessons.stream()
                .mapToInt(lesson -> lessonXPMap.getOrDefault(lesson.getId(), 0))
                .sum();

        if (!topic.isPremium()) {
            return TopicResponse.builder()
                    .id(topic.getId())
                    .name(topic.getName())
                    .imageUrl(topic.getImageUrl())
                    .displayOrder(topic.getDisplayOrder())
                    .isPremium(topic.isPremium())
                    .totalUserXPPoints(topicTotalXp)
                    .build();
        }

        // Topics 1 and 2 are always free, others require subscription
        boolean isPremium = topic.getId() != 1 && topic.getId() != 2 && !isUserSubscribed;

        return TopicResponse.builder()
                .id(topic.getId())
                .name(topic.getName())
                .imageUrl(topic.getImageUrl())
                .displayOrder(topic.getDisplayOrder())
                .isPremium(isPremium)
                .totalUserXPPoints(topicTotalXp)
                .build();
    }

    /**
     * Retrieves review data for the currently authenticated user.
     * This includes:
     * <ul>
     *  <li>Flash cards of vocabulary words from completed lessons</li>
     *  <li>Previously learned material organized by topics, including:</li>
     *  <ul>
     *      <li>Vocabulary with example sentences</li>
     *      <li>Dialogues with context and lines</li>
     *  </ul>
     * </ul>
     *
     *
     * The method filters lessons by the CORE_LESSON type and groups them by topics
     * to efficiently process and organize the review materials.
     *
     * @return A structured ReviewResponse containing flash cards and previously learned material
     * @throws ResourceNotFoundException if the current user does not exist
     */
    @Override
    public ReviewResponse retrieveReviewData() {
        // Retrieve user's id
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new ResourceNotFoundException(ErrorCodeEnum.USER_NOT_EXISTED);
        }

        // Find all lessons which user learned and have the lessonType = CORE_LESSON
        List<Lesson> lessons = lessonRepository.findByUserLessonAttemptsUserIdAndLessonTypeId(
                userId, LessonTypeEnum.CORE_LESSON.getId());

        if (lessons.isEmpty()) {
            return ReviewResponse.builder().flashCards(new ArrayList<>()).build();
        }

        // Group lessons by topics to avoid repeated topic lookups
        Map<Topic, List<Lesson>> lessonsByTopic = lessons.stream()
                .collect(Collectors.groupingBy(Lesson::getTopic));

        List<TopicReviewResponse<Object>> flashCards = buildFlashCards(lessonsByTopic);
        PreviousLearnedResponse previousLearned = buildPreviousLearnedResponse(lessonsByTopic);
        List<Object> reviewExams = buildReviewExams(lessonsByTopic);

        return ReviewResponse.builder()
                .exercises(reviewExams.size() >= 2 ? reviewExams : null)
                .flashCards(flashCards)
                .previousLearned(previousLearned)
                .build();
    }

    /**
     * Builds review exam exercises from lessons the user has completed.
     * This method:
     * 1. Retrieves all topics sorted by display order
     * 2. For each topic, collects multiple choice and word arrangement exercises
     * 3. Randomizes and selects up to 5 exercises of each type
     * 4. Combines them in alternating order with sequential display order
     *
     * @param lessonsByTopic Map of topics to their associated lessons
     * @return A list of exercise response objects ready for review
     */
    private List<Object> buildReviewExams(Map<Topic, List<Lesson>> lessonsByTopic) {
        List<Object> exerciseDetails = new ArrayList<>();
        List<Map.Entry<Topic, List<Lesson>>> sortedTopics = getSortedTopics(lessonsByTopic);

        for (Map.Entry<Topic, List<Lesson>> topicEntry : sortedTopics) {
            List<Lesson> lessons = topicEntry.getValue();
            List<Exercise> multipleChoiceExercises = new ArrayList<>();
            List<Exercise> wordArrangementExercises = new ArrayList<>();

            // Collect exercises from all lessons in this topic
            collectExercisesFromLessons(lessons, multipleChoiceExercises, wordArrangementExercises);

            // Randomize and add exercises to result list in alternating order
            addExercisesInAlternatingOrder(multipleChoiceExercises, wordArrangementExercises, exerciseDetails);
        }

        return exerciseDetails;
    }

    /**
     * Collects multiple choice and word arrangement exercises from lessons.
     *
     * @param lessons List of lessons to extract exercises from
     * @param multipleChoiceExercises List to populate with multiple choice exercises
     * @param wordArrangementExercises List to populate with word arrangement exercises
     */
    private void collectExercisesFromLessons(List<Lesson> lessons,
                                           List<Exercise> multipleChoiceExercises,
                                           List<Exercise> wordArrangementExercises) {
        for (Lesson lesson : lessons) {
            multipleChoiceExercises.addAll(exerciseRepository
                    .findByLessonIdAndExerciseTypeId(lesson.getId(),
                            ExerciseTypeEnum.MULTIPLE_CHOICE_EXERCISE.getId()));

            wordArrangementExercises.addAll(exerciseRepository
                    .findByLessonIdAndExerciseTypeId(lesson.getId(),
                            ExerciseTypeEnum.WORD_ARRANGEMENT_EXERCISE.getId()));
        }
    }

    /**
     * Randomizes exercise lists, selects limited quantities, and adds them
     * to the result list in alternating order with sequential display orders.
     *
     * @param multipleChoiceExercises List of multiple choice exercises
     * @param wordArrangementExercises List of word arrangement exercises
     * @param resultList List to add formatted exercise responses to
     */
    private void addExercisesInAlternatingOrder(List<Exercise> multipleChoiceExercises,
                                              List<Exercise> wordArrangementExercises,
                                              List<Object> resultList) {
        // Randomize both lists
        Collections.shuffle(multipleChoiceExercises);
        Collections.shuffle(wordArrangementExercises);

        // Take up to 5 exercises of each type
        int mcCount = Math.min(5, multipleChoiceExercises.size());
        int waCount = Math.min(5, wordArrangementExercises.size());

        // Add exercises in alternating order with sequential display order
        int currentDisplayOrder = 1;
        for (int i = 0; i < Math.max(mcCount, waCount); i++) {
            if (i < mcCount) {
                Exercise exercise = multipleChoiceExercises.get(i);
                exercise.setDisplayOrder(currentDisplayOrder++);
                resultList.add(topicService.buildMultipleChoiceExerciseResponse(exercise));
            }
            if (i < waCount) {
                Exercise exercise = wordArrangementExercises.get(i);
                exercise.setDisplayOrder(currentDisplayOrder++);
                resultList.add(topicService.buildWordArrangementExerciseResponse(exercise));
            }
        }
    }

    /**
     * Builds the flash cards section of the review data from lessons grouped by topics.
     * Flash cards are vocabulary items presented in a format suitable for study and review.
     *
     * @param lessonsByTopic Map containing topics as keys and their associated lessons as values
     * @return A list of TopicReviewResponse objects containing flash card data
     */
    private List<TopicReviewResponse<Object>> buildFlashCards(Map<Topic, List<Lesson>> lessonsByTopic) {
        return getSortedTopics(lessonsByTopic).stream()
                .map(entry -> {
                    Topic topic = entry.getKey();
                    List<Lesson> topicLessons = entry.getValue();

                    // Extract words and map them to flash card items
                    List<Word> words = extractWordsFromLessons(topicLessons, ExerciseTypeEnum.VOCABULARY_EXERCISE);
                    List<FlashCardItemResponse> flashCardItems = mapWordsToFlashCardItems(words);

                    // Build and return the topic review response
                    return TopicReviewResponse.builder()
                            .id(topic.getId())
                            .name(topic.getName())
                            .displayOrder(topic.getDisplayOrder())
                            .data(new ArrayList<>(flashCardItems))
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Maps Word entities to FlashCardItemResponse objects.
     *
     * @param words The word entities to map
     * @return A list of FlashCardItemResponse objects
     */
    private List<FlashCardItemResponse> mapWordsToFlashCardItems(List<Word> words) {
        return words.stream()
                .map(word -> FlashCardItemResponse.builder()
                        .vietnameseText(word.getVietnameseText())
                        .englishText(word.getEnglishText())
                        .imageUrl(word.getImageUrl())
                        .audioUrl(word.getAudioUrl())
                        .build())
                .toList();
    }

    /**
     * Builds the "Previous Learned" section of the review data from lessons grouped by topics.
     * This method processes vocabulary and dialogue data from completed lessons.
     *
     * @param lessonsByTopic Map containing topics as keys and their associated lessons as values
     * @return A structured response containing vocabularies and dialogues organized by topics
     */
    private PreviousLearnedResponse buildPreviousLearnedResponse(Map<Topic, List<Lesson>> lessonsByTopic) {
        List<TopicReviewResponse<Object>> vocabularies = new ArrayList<>();
        List<TopicReviewResponse<Object>> dialogues = new ArrayList<>();

        // Get sorted topics by display order for consistent presentation
        getSortedTopics(lessonsByTopic).forEach(entry -> {
            Topic topic = entry.getKey();
            List<Lesson> lessons = entry.getValue();

            // Process vocabularies and dialogues for the current topic
            processVocabularies(topic, lessons, vocabularies);
            processDialogues(topic, lessons, dialogues);
        });

        return PreviousLearnedResponse.builder()
                .vocabularies(vocabularies)
                .dialogues(dialogues)
                .build();
    }

    /**
     * Gets topics sorted by their display order for consistent presentation.
     *
     * @param lessonsByTopic Map of topics to their associated lessons
     * @return A sorted list of map entries ordered by topic display order
     */
    private List<Map.Entry<Topic, List<Lesson>>> getSortedTopics(Map<Topic, List<Lesson>> lessonsByTopic) {
        return lessonsByTopic.entrySet().stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getDisplayOrder()))
                .toList();
    }

    /**
     * Processes vocabulary data for a specific topic and adds it to the vocabularies list.
     * Extracts vocabulary words from lessons and maps them to response objects.
     *
     * @param topic        The current topic being processed
     * @param lessons      The lessons associated with this topic
     * @param vocabularies The list to which the vocabulary response will be added
     */
    private void processVocabularies(Topic topic, List<Lesson> lessons, List<TopicReviewResponse<Object>> vocabularies) {
        // Extract all words from vocabulary exercises in the topic's lessons
        List<Word> words = extractWordsFromLessons(lessons, ExerciseTypeEnum.VOCABULARY_EXERCISE);

        // Map words to VocabularyResponse objects
        List<VocabularyResponse> vocabularyResponses = words.stream()
                .map(this::mapWordToVocabularyResponse)
                .toList();

        // Add the vocabulary data for this topic if there are any vocabulary items
        if (!vocabularyResponses.isEmpty()) {
            addDataToTopicReviewList(topic, vocabularyResponses, vocabularies);
        }
    }

    /**
     * Extracts Word entities from lessons based on the exercise type.
     *
     * @param lessons      The lessons to extract words from
     * @param exerciseType The type of exercise to filter by
     * @return A list of Word entities extracted from the lessons
     */
    private List<Word> extractWordsFromLessons(List<Lesson> lessons, ExerciseTypeEnum exerciseType) {
        return lessons.stream()
                .flatMap(lesson -> exerciseRepository
                        .findByLessonIdAndExerciseTypeId(lesson.getId(), exerciseType.getId())
                        .stream()
                        .map(exercise -> exercise.getVocabularyExercise().getWord()))
                .toList();
    }

    /**
     * Maps a Word entity to a VocabularyResponse DTO.
     * Includes word information and an associated sentence.
     *
     * @param word The word entity to map
     * @return The corresponding VocabularyResponse
     * @throws ResourceNotFoundException if the word has no associated sentences
     */
    private VocabularyResponse mapWordToVocabularyResponse(Word word) {
        return VocabularyResponse.builder()
                .vietnameseText(word.getVietnameseText())
                .englishText(word.getEnglishText())
                .imageUrl(word.getImageUrl())
                .audioUrl(word.getAudioUrl())
                .sentence(word.getSentences().stream()
                        .map(s -> VocabularyResponse.SentenceResponse.builder()
                                .vietnameseText(s.getVietnameseText())
                                .englishText(s.getEnglishText())
                                .audioUrl(s.getAudioUrl())
                                .build())
                        .findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED)))
                .build();
    }

    /**
     * Processes dialogue data for a specific topic and adds it to the dialogues list.
     * Extracts dialogue exercises from lessons and maps them to response objects.
     *
     * @param topic     The current topic being processed
     * @param lessons   The lessons associated with this topic
     * @param dialogues The list to which the dialogue response will be added
     */
    private void processDialogues(Topic topic, List<Lesson> lessons, List<TopicReviewResponse<Object>> dialogues) {
        // Extract all dialogue exercises from the topic's lessons
        List<DialogueExercise> dialogueExercises = extractDialogueExercises(lessons);

        // Map dialogue exercises to DialogueResponse objects
        List<DialogueResponse> dialogueResponses = dialogueExercises.stream()
                .map(this::mapDialogueExerciseToResponse)
                .toList();

        // Add the dialogue data for this topic if there are any dialogue items
        if (!dialogueResponses.isEmpty()) {
            addDataToTopicReviewList(topic, dialogueResponses, dialogues);
        }
    }

    /**
     * Extracts DialogueExercise entities from lessons.
     *
     * @param lessons The lessons to extract dialogue exercises from
     * @return A list of DialogueExercise entities
     */
    private List<DialogueExercise> extractDialogueExercises(List<Lesson> lessons) {
        return lessons.stream()
                .flatMap(lesson -> exerciseRepository
                        .findByLessonIdAndExerciseTypeId(lesson.getId(), ExerciseTypeEnum.DIALOGUE_EXERCISE.getId())
                        .stream()
                        .map(exercise -> exercise.getDialogueExercise()))
                .toList();
    }

    /**
     * Maps a DialogueExercise entity to a DialogueResponse DTO.
     * Includes context and ordered dialogue lines.
     *
     * @param dialogueExercise The dialogue exercise entity to map
     * @return The corresponding DialogueResponse
     */
    private DialogueResponse mapDialogueExerciseToResponse(DialogueExercise dialogueExercise) {
        return DialogueResponse.builder()
                .context(dialogueExercise.getContext())
                .dialogueExerciseLines(dialogueExercise.getDialogueExerciseLines().stream()
                        .map(line -> DialogueLineResponse.builder()
                                .isChangeSpeaker(SpeakerEnum.A.equals(line.getSpeaker()))
                                .englishText(line.getEnglishText())
                                .vietnameseText(line.getVietnameseText())
                                .audioUrl(line.getAudioUrl())
                                .displayOrder(line.getDisplayOrder())
                                .build())
                        .toList())
                .build();
    }

    /**
     * Adds data to a TopicReviewResponse list with the topic's information.
     *
     * @param topic     The topic to include in the response
     * @param data      The data to add to the topic's response
     * @param topicList The list to add the topic response to
     * @param <T> type of data want to add
     */
    private <T> void addDataToTopicReviewList(Topic topic, List<T> data, List<TopicReviewResponse<Object>> topicList) {
        topicList.add(TopicReviewResponse.builder()
                .id(topic.getId())
                .name(topic.getName())
                .displayOrder(topic.getDisplayOrder())
                .data(new ArrayList<>(data))
                .build());
    }

    @Override
    public List<ConversationResponse> retrieveConversationData() {
        List<Conversation> conversations = conversationRepository.findByOrderByDisplayOrderAsc();
        boolean isUserSubscribed = userSubscriptionRepository
                .existsByIsActiveAndPaymentStatusAndUserId(true, PaymentStatusEnum.SUCCEEDED.getValue(), SecurityUtils.getCurrentUserId());

        List<ConversationResponse> conversationResponses = new ArrayList<>();
        for (Conversation c : conversations) {

            // Topics 1 and 2 are always free, others require subscription
            boolean isPremium = c.getId() != 1 && c.getId() != 2 && !isUserSubscribed;

            ConversationResponse conversationResponse = ConversationResponse.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .imageUrl(c.getImageUrl())
                    .displayOrder(c.getDisplayOrder())
                    .isPremium(isPremium)
                    .build();
            conversationResponses.add(conversationResponse);
        }

        return conversationResponses;
    }
}
