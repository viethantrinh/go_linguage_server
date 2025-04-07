package tech.trvihnls.features.topic.services.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.trvihnls.commons.domains.*;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.ExerciseRepository;
import tech.trvihnls.commons.repositories.LessonRepository;
import tech.trvihnls.commons.repositories.TopicRepository;
import tech.trvihnls.commons.repositories.UserLessonAttemptRepository;
import tech.trvihnls.commons.utils.SecurityUtils;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.commons.utils.enums.SpeakerEnum;
import tech.trvihnls.features.excercise.dtos.response.*;
import tech.trvihnls.features.lesson.dtos.response.LessonDetailResponse;
import tech.trvihnls.features.topic.dtos.response.TopicAdminResponse;
import tech.trvihnls.features.topic.mapper.TopicMapper;
import tech.trvihnls.features.topic.services.TopicService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing topics.
 */
@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final UserLessonAttemptRepository userLessonAttemptRepository;
    private final ExerciseRepository exerciseRepository;
    private final TopicMapper topicMapper;
    private final EntityManager entityManager;

    /**
     * Returns detailed lesson information with all associated exercises for a given topic.
     * This method retrieves lessons by topic ID, calculates user XP points, and assembles
     * a comprehensive response with exercise details.
     *
     * @param topicId ID of the topic to retrieve lessons for
     * @return A list of lesson detail responses with nested exercise information
     * @throws ResourceNotFoundException if the topic or associated resources do not exist
     */
    @Override
    public List<LessonDetailResponse> getTopicLessonDetailResponseData(long topicId) {
        // Retrieve all lessons for the topic ordered by display order
        List<Lesson> lessons = lessonRepository.findByTopicIdOrderByDisplayOrderAsc(topicId);

        // Calculate user XP points for each lesson
        Map<Long, Integer> lessonXpPointsMap = calculateUserXpPoints(lessons);

        // Build and return the detailed lesson responses
        return buildLessonDetailResponses(lessons, lessonXpPointsMap, topicId);
    }

    @Override
    public List<TopicAdminResponse> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream().map(topicMapper::toTopicResponse).toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // Check if topic exists
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.TOPIC_NOT_EXISTED));

        // Clear many-to-many relationships with words and sentences via direct SQL
        entityManager.createNativeQuery(
                        "DELETE FROM tbl_topic_word WHERE topic_id = :topicId")
                .setParameter("topicId", id)
                .executeUpdate();

        entityManager.createNativeQuery(
                        "DELETE FROM tbl_topic_sentence WHERE topic_id = :topicId")
                .setParameter("topicId", id)
                .executeUpdate();

        // Get all lessons for this topic
        List<Lesson> lessons = lessonRepository.findByTopicIdOrderByDisplayOrderAsc(id);

        // Delete exercise relationships before deleting exercises
        for (Lesson lesson : lessons) {
            List<Exercise> exercises = exerciseRepository.findByLessonId(lesson.getId());
            for (Exercise exercise : exercises) {
                deleteExerciseRelationships(exercise);
            }
        }

        for (Lesson lesson : lessons) {
            userLessonAttemptRepository.deleteAllByLessonId(lesson.getId());
        }

        for (Lesson lesson : lessons) {
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_lesson WHERE tbl_lesson.id = :lessonId")
                    .setParameter("lessonId", lesson.getId())
                    .executeUpdate();
        }


        entityManager.createNativeQuery(
                        "DELETE FROM tbl_topic WHERE tbl_topic.id = :topicId")
                .setParameter("topicId", topic.getId())
                .executeUpdate();
    }

    private void deleteExerciseRelationships(Exercise exercise) {
        // For vocabulary exercises: need to delete (not nullify) due to composite key
        if (exercise.getVocabularyExercise() != null) {
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_vocabulary_exercise WHERE exercise_id = :exerciseId")
                    .setParameter("exerciseId", exercise.getId())
                    .executeUpdate();
        }

        // For dialogue exercises
        if (exercise.getDialogueExercise() != null) {
            Long deId = exercise.getDialogueExercise().getId();

            // Delete dialogue exercise lines first
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_dialogue_exercise_line WHERE dialogue_exercise_id = :deId")
                    .setParameter("deId", deId)
                    .executeUpdate();

            // Then delete the dialogue exercise itself
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_dialogue_exercise WHERE id = :deId")
                    .setParameter("deId", deId)
                    .executeUpdate();
        }

        // For multiple choice exercises
        if (exercise.getMultipleChoiceExercise() != null) {
            Long mceId = exercise.getMultipleChoiceExercise().getId();

            // Delete options first
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_multiple_choice_option WHERE multiple_choice_exercise_id = :mceId")
                    .setParameter("mceId", mceId)
                    .executeUpdate();

            // Then delete the exercise itself
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_multiple_choice_exercise WHERE id = :mceId")
                    .setParameter("mceId", mceId)
                    .executeUpdate();
        }

        // For word arrangement exercises
        if (exercise.getWordArrangementExercise() != null) {
            Long waeId = exercise.getWordArrangementExercise().getId();

            // Delete options first
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_word_arrangement_option WHERE word_arrangement_exercise_id = :waeId")
                    .setParameter("waeId", waeId)
                    .executeUpdate();

            // Then delete the exercise
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_word_arrangement_exercise WHERE id = :waeId")
                    .setParameter("waeId", waeId)
                    .executeUpdate();
        }

        // For matching exercises
        if (exercise.getMatchingExercise() != null) {
            Long meId = exercise.getMatchingExercise().getId();

            // Delete matching pairs first
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_matching_pair WHERE matching_exercise_id = :meId")
                    .setParameter("meId", meId)
                    .executeUpdate();

            // Then delete the exercise
            entityManager.createNativeQuery(
                            "DELETE FROM tbl_matching_exercise WHERE id = :meId")
                    .setParameter("meId", meId)
                    .executeUpdate();
        }

        // Delete the base exercise
        entityManager.createNativeQuery(
                        "DELETE FROM tbl_exercise WHERE id = :exerciseId")
                .setParameter("exerciseId", exercise.getId())
                .executeUpdate();
    }

    /**
     * Calculates the XP points earned by the current user for each lesson.
     *
     * @param lessons List of lessons to calculate XP points for
     * @return A map of lesson IDs to XP points earned
     */
    private Map<Long, Integer> calculateUserXpPoints(List<Lesson> lessons) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<UserLessonAttempt> userAttemptedLessons = userLessonAttemptRepository.findByUserId(userId);

        // Create a Set of lesson IDs for faster lookup
        Set<Long> lessonIds = lessons.stream()
                .map(Lesson::getId)
                .collect(Collectors.toSet());

        // Build a map of lesson IDs to XP points
        return userAttemptedLessons.stream()
                .filter(attempt -> lessonIds.contains(attempt.getLesson().getId()))
                .collect(Collectors.toMap(
                        attempt -> attempt.getLesson().getId(),
                        UserLessonAttempt::getXpPointsEarned
                ));
    }

    /**
     * Builds detailed lesson responses including all exercises.
     *
     * @param lessons           List of lessons to build responses for
     * @param lessonXpPointsMap Map of lesson IDs to XP points
     * @param topicId           ID of the parent topic
     * @return List of detailed lesson responses
     */
    private List<LessonDetailResponse> buildLessonDetailResponses(
            List<Lesson> lessons, Map<Long, Integer> lessonXpPointsMap, long topicId) {

        return lessons.stream()
                .map(lesson -> {
                    List<Object> exerciseDetails = buildExerciseDetails(lesson, topicId);

                    return LessonDetailResponse.builder()
                            .id(lesson.getId())
                            .name(lesson.getName())
                            .totalUserXPPoints(lessonXpPointsMap.getOrDefault(lesson.getId(), 0))
                            .lessonType(lesson.getLessonType().getId().intValue())
                            .displayOrder(lesson.getDisplayOrder())
                            .exercises(exerciseDetails)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Builds exercise details for a specific lesson.
     *
     * @param lesson  The lesson to build exercise details for
     * @param topicId ID of the parent topic
     * @return List of exercise details
     */
    private List<Object> buildExerciseDetails(Lesson lesson, long topicId) {
        List<Object> exerciseDetails = new ArrayList<>();

        // Get all exercises for the lesson ordered by display order
        List<Exercise> exercises = exerciseRepository.findByLessonIdOrderByDisplayOrderAsc(lesson.getId());

        // Process each exercise based on its type
        exercises.forEach(exercise -> addExerciseToDetailsList(exercise, exerciseDetails));

        // Add special exercises based on lesson type
        if (lesson.getLessonType().getId() == 2) { // Speaking lesson
            buildSpeakingExerciseResponse(topicId, exerciseDetails);
        } else if (lesson.getLessonType().getId() == 3) { // Exam lesson
            buildExercisesForExamLesson(topicId, exerciseDetails);
        }

        return exerciseDetails;
    }

    /**
     * Adds an exercise to the details list based on its type.
     *
     * @param exercise        The exercise to add
     * @param exerciseDetails The list to add the exercise to
     */
    private void addExerciseToDetailsList(Exercise exercise, List<Object> exerciseDetails) {
        if (exercise.getVocabularyExercise() != null) {
            exerciseDetails.add(buildVocabularyExerciseResponse(exercise));
        } else if (exercise.getMultipleChoiceExercise() != null) {
            exerciseDetails.add(buildMultipleChoiceExerciseResponse(exercise));
        } else if (exercise.getMatchingExercise() != null) {
            exerciseDetails.add(buildMatchingExerciseResponse(exercise));
        } else if (exercise.getWordArrangementExercise() != null) {
            exerciseDetails.add(buildWordArrangementExerciseResponse(exercise));
        } else if (exercise.getDialogueExercise() != null) {
            exerciseDetails.add(buildDialogueExerciseResponse(exercise));
        }
    }

    /**
     * Builds a response for a vocabulary exercise.
     * This maps a Word entity to a vocabulary exercise response including:
     * - Word text in English and Vietnamese
     * - Associated image and audio
     * - A related sentence for context
     *
     * @param exercise The vocabulary exercise entity
     * @return A structured response with vocabulary details
     * @throws ResourceNotFoundException if the word has no associated sentences
     */
    private ExerciseDetailResponse<VocabularyExerciseResponse> buildVocabularyExerciseResponse(Exercise exercise) {
        Word word = exercise.getVocabularyExercise().getWord();
        List<Sentence> relatedSentences = word.getSentences();

        if (relatedSentences.isEmpty()) {
            throw new ResourceNotFoundException(ErrorCodeEnum.RELATED_SENTENCES_NOT_EXISTED);
        }

        // Get the first sentence for context
        Sentence firstSentence = relatedSentences.get(0);
        VocabularyExerciseResponse.SentenceResponse sentenceResponse = createSentenceResponse(firstSentence);

        // Build the vocabulary response with word data and sentence
        VocabularyExerciseResponse vocabularyExerciseResponse = VocabularyExerciseResponse.builder()
                .englishText(word.getEnglishText())
                .vietnameseText(word.getVietnameseText())
                .imageUrl(word.getImageUrl())
                .audioUrl(word.getAudioUrl())
                .sentence(sentenceResponse)
                .build();

        // Wrap in the standard exercise response format
        return createExerciseDetailResponse(exercise, vocabularyExerciseResponse);
    }

    /**
     * Creates a sentence response from a Sentence entity.
     *
     * @param sentence The sentence entity
     * @return A structured sentence response
     */
    private VocabularyExerciseResponse.SentenceResponse createSentenceResponse(Sentence sentence) {
        return VocabularyExerciseResponse.SentenceResponse.builder()
                .englishText(sentence.getEnglishText())
                .vietnameseText(sentence.getVietnameseText())
                .audioUrl(sentence.getAudioUrl())
                .build();
    }

    /**
     * Builds a response for a multiple choice exercise.
     * This maps a MultipleChoiceExercise entity to a structured response including:
     * - Question (from either a Word or Sentence)
     * - Multiple options (from either Words or Sentences)
     * - Source and target languages
     * - Question type
     *
     * @param exercise The multiple choice exercise entity
     * @return A structured response with multiple choice details
     * @throws AppException if there's a resource conflict (both Word and Sentence are present)
     */
    @Override
    public ExerciseDetailResponse<MultipleChoiceExerciseResponse> buildMultipleChoiceExerciseResponse(Exercise exercise) {
        MultipleChoiceExercise multipleChoiceExercise = exercise.getMultipleChoiceExercise();

        // Build question from either Word or Sentence
        var questionBuilder = buildMultipleChoiceQuestion(multipleChoiceExercise);

        // Build options list from either Words or Sentences
        List<MultipleChoiceExerciseResponse.Option> options = buildMultipleChoiceOptions(
                multipleChoiceExercise.getMultipleChoiceOptions());

        // Create the complete response
        MultipleChoiceExerciseResponse response = MultipleChoiceExerciseResponse.builder()
                .sourceLanguage(multipleChoiceExercise.getSourceLanguage())
                .targetLanguage(multipleChoiceExercise.getTargetLanguage())
                .questionType(multipleChoiceExercise.getQuestionType())
                .question(questionBuilder.build())
                .options(options)
                .build();

        return createExerciseDetailResponse(exercise, response);
    }

    /**
     * Builds a question for a multiple choice exercise.
     *
     * @param exercise The multiple choice exercise entity
     * @return A question builder with populated fields
     * @throws AppException if both Word and Sentence are present
     */
    private MultipleChoiceExerciseResponse.Question.QuestionBuilder buildMultipleChoiceQuestion(
            MultipleChoiceExercise exercise) {

        var builder = MultipleChoiceExerciseResponse.Question.builder();

        if (exercise.getWord() != null && exercise.getSentence() != null) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        if (exercise.getWord() != null) {
            Word word = exercise.getWord();
            builder.englishText(word.getEnglishText())
                    .vietnameseText(word.getVietnameseText())
                    .imageUrl(word.getImageUrl())
                    .audioUrl(word.getAudioUrl());
        } else if (exercise.getSentence() != null) {
            Sentence sentence = exercise.getSentence();
            builder.englishText(sentence.getEnglishText())
                    .vietnameseText(sentence.getVietnameseText())
                    .audioUrl(sentence.getAudioUrl());
        }

        return builder;
    }

    /**
     * Builds options for a multiple choice exercise.
     *
     * @param options The multiple choice options
     * @return A list of option responses
     * @throws AppException if both Word and Sentence are present in an option
     */
    private List<MultipleChoiceExerciseResponse.Option> buildMultipleChoiceOptions(
            List<MultipleChoiceOption> options) {

        return options.stream()
                .map(option -> {
                    if (option.getWord() != null && option.getSentence() != null) {
                        throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
                    }

                    var builder = MultipleChoiceExerciseResponse.Option.builder()
                            .optionType(option.getOptionType())
                            .isCorrect(option.isCorrect());

                    if (option.getWord() != null) {
                        Word word = option.getWord();
                        builder.englishText(word.getEnglishText())
                                .vietnameseText(word.getVietnameseText())
                                .imageUrl(word.getImageUrl())
                                .audioUrl(word.getAudioUrl());
                    } else if (option.getSentence() != null) {
                        Sentence sentence = option.getSentence();
                        builder.englishText(sentence.getEnglishText())
                                .vietnameseText(sentence.getVietnameseText())
                                .audioUrl(sentence.getAudioUrl());
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Builds a response for a matching exercise.
     * This maps MatchingPairs to a list of matching exercise responses,
     * each containing a word's details for the matching activity.
     *
     * @param exercise The matching exercise entity
     * @return A structured response with matching pairs
     */
    private ExerciseDetailResponse<List<MatchingExerciseResponse>> buildMatchingExerciseResponse(Exercise exercise) {
        List<MatchingExerciseResponse> matchingResponses = exercise.getMatchingExercise().getMatchingPairs()
                .stream()
                .map(pair -> MatchingExerciseResponse.builder()
                        .id(pair.getId())
                        .englishText(pair.getWord().getEnglishText())
                        .vietnameseText(pair.getWord().getVietnameseText())
                        .imageUrl(pair.getWord().getImageUrl())
                        .audioUrl(pair.getWord().getAudioUrl())
                        .build())
                .collect(Collectors.toList());

        return createExerciseDetailResponse(exercise, matchingResponses);
    }

    /**
     * Builds a response for a word arrangement exercise.
     * This maps a WordArrangementExercise to a structured response including:
     * - The sentence to be arranged
     * - Individual words with their correct positions
     * - Information about distractors (words that don't belong)
     * - Source and target languages
     *
     * @param exercise The word arrangement exercise entity
     * @return A structured response with word arrangement details
     * @throws ResourceNotFoundException if the sentence is missing
     */
    @Override
    public ExerciseDetailResponse<WordArrangementExerciseResponse> buildWordArrangementExerciseResponse(Exercise exercise) {
        WordArrangementExercise wordArrangementExercise = exercise.getWordArrangementExercise();
        Sentence sentence = wordArrangementExercise.getSentence();

        if (sentence == null) {
            throw new ResourceNotFoundException(ErrorCodeEnum.RELATED_SENTENCES_NOT_EXISTED);
        }

        // Build sentence response
        var sentenceResponse = WordArrangementExerciseResponse.SentenceResponse.builder()
                .englishText(sentence.getEnglishText())
                .vietnameseText(sentence.getVietnameseText())
                .audioUrl(sentence.getAudioUrl())
                .build();

        // Map word arrangement options to responses
        List<WordArrangementExerciseResponse.WordResponse> wordResponses = wordArrangementExercise.getWordArrangementOptions()
                .stream()
                .map(option -> WordArrangementExerciseResponse.WordResponse.builder()
                        .text(option.getWordText())
                        .isDistractor(option.isDistractor())
                        .correctPosition(option.getCorrectPosition())
                        .build())
                .collect(Collectors.toList());

        // Build complete response
        WordArrangementExerciseResponse response = WordArrangementExerciseResponse.builder()
                .sourceLanguage(wordArrangementExercise.getSourceLanguage())
                .targetLanguage(wordArrangementExercise.getTargetLanguage())
                .sentence(sentenceResponse)
                .words(wordResponses)
                .build();

        return createExerciseDetailResponse(exercise, response);
    }

    /**
     * Builds a response for a dialogue exercise.
     * This maps a DialogueExercise to a structured response including:
     * - Context information
     * - Dialogue lines with speaker changes
     * - Support for dialogue lines with blank words for fill-in exercises
     *
     * @param exercise The dialogue exercise entity
     * @return A structured response with dialogue details
     */
    private ExerciseDetailResponse<DialogueExerciseResponse> buildDialogueExerciseResponse(Exercise exercise) {
        DialogueExercise dialogueExercise = exercise.getDialogueExercise();

        // Map dialogue lines to responses
        List<DialogueExerciseResponse.DialogueLineResponse> dialogueLineResponses = dialogueExercise.getDialogueExerciseLines()
                .stream()
                .map(this::mapDialogueLineToResponse)
                .collect(Collectors.toList());

        // Build complete dialogue response
        DialogueExerciseResponse response = DialogueExerciseResponse.builder()
                .context(dialogueExercise.getContext())
                .dialogueExerciseLines(dialogueLineResponses)
                .build();

        return createExerciseDetailResponse(exercise, response);
    }

    /**
     * Maps a DialogueExerciseLine to a DialogueLineResponse.
     * Handles special formatting for lines with blank words.
     *
     * @param line The dialogue exercise line
     * @return A dialogue line response
     */
    private DialogueExerciseResponse.DialogueLineResponse mapDialogueLineToResponse(DialogueExerciseLine line) {
        var builder = DialogueExerciseResponse.DialogueLineResponse.builder()
                .isChangeSpeaker(line.getSpeaker().equals(SpeakerEnum.A))
                .vietnameseText(line.getVietnameseText())
                .audioUrl(line.getAudioUrl())
                .displayOrder(line.getDisplayOrder())
                .blankWord(line.getBlankWord());

        // Handle blank words in English text
        if (line.isHasBlank()) {
            String blankEnglishText = line.getEnglishText().replace(line.getBlankWord(), "[]");
            builder.englishText(blankEnglishText);
        } else {
            builder.englishText(line.getEnglishText());
        }

        return builder.build();
    }

    /**
     * Builds speaking exercise responses for a speaking lesson.
     * This method:
     * 1. Retrieves words and sentences from the topic
     * 2. Combines and randomizes them
     * 3. Selects up to 6 items
     * 4. Creates speaking exercise responses for each item
     *
     * @param topicId         The ID of the topic
     * @param exerciseDetails The list to add speaking exercises to
     * @throws ResourceNotFoundException if the topic doesn't exist
     */
    private void buildSpeakingExerciseResponse(long topicId, List<Object> exerciseDetails) {
        // Retrieve topic with its words and sentences
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.TOPIC_NOT_EXISTED));

        // Combine words and sentences into a single collection
        List<Object> speakingMaterials = new ArrayList<>();
        speakingMaterials.addAll(topic.getWords());
        speakingMaterials.addAll(topic.getSentences());

        // Randomize and limit selection
        Collections.shuffle(speakingMaterials);
        int itemsToTake = Math.min(6, speakingMaterials.size());
        List<Object> selectedMaterials = speakingMaterials.subList(0, itemsToTake);

        // Convert selected materials to speaking exercise responses
        selectedMaterials.forEach(material -> {
            SpeakingExerciseResponse response;

            if (material instanceof Word word) {
                response = buildSpeakingResponseFromWord(word);
            } else if (material instanceof Sentence sentence) {
                response = buildSpeakingResponseFromSentence(sentence);
            } else {
                return; // Skip if neither Word nor Sentence
            }

            exerciseDetails.add(response);
        });
    }

    /**
     * Creates a speaking exercise response from a Word.
     *
     * @param word The word entity
     * @return A speaking exercise response
     */
    private SpeakingExerciseResponse buildSpeakingResponseFromWord(Word word) {
        return SpeakingExerciseResponse.builder()
                .englishText(word.getEnglishText())
                .vietnameseText(word.getVietnameseText())
                .audioUrl(word.getAudioUrl())
                .build();
    }

    /**
     * Creates a speaking exercise response from a Sentence.
     *
     * @param sentence The sentence entity
     * @return A speaking exercise response
     */
    private SpeakingExerciseResponse buildSpeakingResponseFromSentence(Sentence sentence) {
        return SpeakingExerciseResponse.builder()
                .englishText(sentence.getEnglishText())
                .vietnameseText(sentence.getVietnameseText())
                .audioUrl(sentence.getAudioUrl())
                .build();
    }

    /**
     * Builds exercises for an exam lesson by:
     * 1. Finding exercises from core lessons in the same topic
     * 2. Filtering for multiple choice and word arrangement exercises
     * 3. Randomly selecting up to 7 of each type
     * 4. Adding them to the exercise list in alternating order
     *
     * @param topicId         The ID of the topic
     * @param exerciseDetails The list to add exam exercises to
     */
    private void buildExercisesForExamLesson(long topicId, List<Object> exerciseDetails) {
        // Get all exercises from core lessons (type 1) in this topic
        List<Exercise> coreExercises = findExercisesFromCoreLessons(topicId);

        // Separate exercises by type
        Map<Class<?>, List<Exercise>> exercisesByType = categorizeExercisesByType(coreExercises);
        List<Exercise> multipleChoiceExercises = exercisesByType.getOrDefault(MultipleChoiceExercise.class, new ArrayList<>());
        List<Exercise> wordArrangementExercises = exercisesByType.getOrDefault(WordArrangementExercise.class, new ArrayList<>());

        // Randomize and limit exercise selections
        Collections.shuffle(multipleChoiceExercises);
        Collections.shuffle(wordArrangementExercises);
        int mcCount = Math.min(7, multipleChoiceExercises.size());
        int waCount = Math.min(7, wordArrangementExercises.size());

        // Add exercises to the list in alternating order
        addExercisesToExamLesson(
                multipleChoiceExercises.subList(0, mcCount),
                wordArrangementExercises.subList(0, waCount),
                exerciseDetails
        );
    }

    /**
     * Finds all exercises from core lessons in a topic.
     *
     * @param topicId The ID of the topic
     * @return List of exercises from core lessons
     */
    private List<Exercise> findExercisesFromCoreLessons(long topicId) {
        List<Lesson> coreLessons = lessonRepository.findByTopicIdAndLessonTypeId(topicId, 1L);

        return coreLessons.stream()
                .flatMap(lesson -> exerciseRepository.findByLessonId(lesson.getId()).stream())
                .collect(Collectors.toList());
    }

    /**
     * Categorizes exercises by their type.
     *
     * @param exercises List of exercises to categorize
     * @return Map of exercise types to lists of exercises
     */
    private Map<Class<?>, List<Exercise>> categorizeExercisesByType(List<Exercise> exercises) {
        Map<Class<?>, List<Exercise>> exercisesByType = new HashMap<>();

        exercises.forEach(exercise -> {
            if (exercise.getMultipleChoiceExercise() != null) {
                exercisesByType.computeIfAbsent(MultipleChoiceExercise.class, k -> new ArrayList<>()).add(exercise);
            } else if (exercise.getWordArrangementExercise() != null) {
                exercisesByType.computeIfAbsent(WordArrangementExercise.class, k -> new ArrayList<>()).add(exercise);
            }
        });

        return exercisesByType;
    }

    /**
     * Adds exercises to the exam lesson in alternating order.
     *
     * @param mcExercises     Multiple choice exercises
     * @param waExercises     Word arrangement exercises
     * @param exerciseDetails The list to add exercises to
     */
    private void addExercisesToExamLesson(
            List<Exercise> mcExercises,
            List<Exercise> waExercises,
            List<Object> exerciseDetails) {

        int currentDisplayOrder = 1;
        int maxExercises = Math.max(mcExercises.size(), waExercises.size());

        for (int i = 0; i < maxExercises; i++) {
            if (i < mcExercises.size()) {
                Exercise exercise = mcExercises.get(i);
                exercise.setDisplayOrder(currentDisplayOrder++);
                exerciseDetails.add(buildMultipleChoiceExerciseResponse(exercise));
            }

            if (i < waExercises.size()) {
                Exercise exercise = waExercises.get(i);
                exercise.setDisplayOrder(currentDisplayOrder++);
                exerciseDetails.add(buildWordArrangementExerciseResponse(exercise));
            }
        }
    }

    /**
     * Creates a standardized exercise detail response with common fields.
     *
     * @param exercise The exercise entity
     * @param data     The specific exercise data
     * @return A standardized exercise detail response
     */
    private <T> ExerciseDetailResponse<T> createExerciseDetailResponse(Exercise exercise, T data) {
        return ExerciseDetailResponse.<T>builder()
                .id(exercise.getId())
                .instruction(exercise.getInstruction())
                .displayOrder(exercise.getDisplayOrder())
                .data(data)
                .build();
    }
}
