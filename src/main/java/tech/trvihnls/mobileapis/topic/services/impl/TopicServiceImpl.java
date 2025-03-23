package tech.trvihnls.mobileapis.topic.services.impl;

    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
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
    import tech.trvihnls.mobileapis.excercise.dtos.response.*;
    import tech.trvihnls.mobileapis.lesson.dtos.response.LessonDetailResponse;
    import tech.trvihnls.mobileapis.topic.services.TopicService;

    import java.util.*;

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

        /**
         * Return all lessons and exercises inside that lesson for details.
         *
         * @param topicId id's of the topic
         * @return all lessons and exercises inside that lesson for details (lessons owned by topic)
         */
        @Override
        public List<LessonDetailResponse> getTopicLessonDetailResponseData(long topicId) {
            // Find all related lessons belong to these topic (find list of lessons which have this topicId)
            List<Lesson> lessons = lessonRepository.findByTopicIdOrderByDisplayOrderAsc(topicId);

            // Calculate the user's xp points for lesson scope of this user (map the lesson's id with the totalUserXpPoints of that lesson)
            Long userId = SecurityUtils.getCurrentUserId();
            List<UserLessonAttempt> userAttemptedLessons = userLessonAttemptRepository.findByUserId(userId); // find all the lessons which attempted by specific user
            Map<Long, Integer> lessonXpPointsMap = new HashMap<>();
            for (var u : userAttemptedLessons) {
                if (lessons.contains(u.getLesson())) {
                    lessonXpPointsMap.put(u.getLesson().getId(), u.getXpPointsEarned());
                }
            }

            // Build list of lessonDetailResponse
            List<LessonDetailResponse> lessonDetails = new ArrayList<>();
            for (var l : lessons) {
                // Build list of exerciseDetailResponse
                List<Object> exerciseDetails = new ArrayList<>();

                // Find all the exercise belong to this lesson by display order
                List<Exercise> exercises = exerciseRepository.findByLessonIdOrderByDisplayOrderAsc(l.getId());

                // Loop through each exercise and build xExerciseResponse (eg: VocabularyExerciseResponse, MultipleChoiceExerciseResponse,...)
                for (var e : exercises) {
                    if (e.getVocabularyExercise() != null) {
                        exerciseDetails.add(buildVocabularyExerciseResponse(e));
                    }

                    if (e.getMultipleChoiceExercise() != null) {
                        exerciseDetails.add(buildMultipleChoiceExerciseResponse(e));
                    }

                    if (e.getMatchingExercise() != null) {
                        exerciseDetails.add(buildMatchingExerciseResponse(e));
                    }

                    if (e.getWordArrangementExercise() != null) {
                        exerciseDetails.add(buildWordArrangementExerciseResponse(e));
                    }

                    if (e.getDialogueExercise() != null) {
                        exerciseDetails.add(buildDialogueExerciseResponse(e));
                    }
                }

                if (l.getLessonType().getId() == 2) { // If the lesson is speaking lesson
                    buildSpeakingExerciseResponse(topicId, exerciseDetails);
                }

                if (l.getLessonType().getId() == 3) { // If the lesson is exam lesson
                    buildExercisesForExamLesson(topicId, exerciseDetails);

                }

                LessonDetailResponse lessonDetailResponse = LessonDetailResponse.builder()
                        .id(l.getId())
                        .name(l.getName())
                        .totalUserXPPoints(lessonXpPointsMap.getOrDefault(l.getId(), 0))
                        .lessonType(l.getLessonType().getId().intValue())
                        .displayOrder(l.getDisplayOrder())
                        .exercises(exerciseDetails)
                        .build();

                lessonDetails.add(lessonDetailResponse);
            }

            return lessonDetails;
        }

        /**
         * Build a response for a vocabulary exercise.
         *
         * @param exercise the exercise entity
         * @return the response for the vocabulary exercise
         */
        private ExerciseDetailResponse<VocabularyExerciseResponse> buildVocabularyExerciseResponse(Exercise exercise) {

            // Find the word related to this vocabulary exercise
            Word word = exercise.getVocabularyExercise().getWord();

            // Find the related sentences and pick random one
            List<Sentence> relatedSentence = word.getSentences();

            if (relatedSentence.isEmpty()) {
                throw new ResourceNotFoundException(ErrorCodeEnum.RELATED_SENTENCES_NOT_EXISTED);
            }

            VocabularyExerciseResponse vocabularyExerciseResponse = VocabularyExerciseResponse.builder()
                    .englishText(word.getEnglishText())
                    .vietnameseText(word.getVietnameseText())
                    .imageUrl(word.getImageUrl())
                    .audioUrl(word.getAudioUrl())
                    .sentence(
                            VocabularyExerciseResponse.SentenceResponse.builder()
                                    .englishText(relatedSentence.get(0).getEnglishText())
                                    .vietnameseText(relatedSentence.get(0).getVietnameseText())
                                    .audioUrl(relatedSentence.get(0).getAudioUrl())
                                    .build()
                    )
                    .build();

            return ExerciseDetailResponse.<VocabularyExerciseResponse>builder()
                    .id(exercise.getId())
                    .instruction(exercise.getInstruction())
                    .displayOrder(exercise.getDisplayOrder())
                    .data(vocabularyExerciseResponse)
                    .build();
        }

        /**
         * Build a response for a multiple choice exercise.
         *
         * @param exercise the exercise entity
         * @return the response for the multiple choice exercise
         */
        private ExerciseDetailResponse<MultipleChoiceExerciseResponse> buildMultipleChoiceExerciseResponse(Exercise exercise) {

            MultipleChoiceExercise multipleChoiceExercise = exercise.getMultipleChoiceExercise();

            var questionBuilder = MultipleChoiceExerciseResponse.Question.builder();

            if (multipleChoiceExercise.getWord() != null && multipleChoiceExercise.getSentence() != null) {
                throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
            }

            if (multipleChoiceExercise.getWord() != null) {
                Word word = multipleChoiceExercise.getWord();
                questionBuilder
                        .englishText(word.getEnglishText())
                        .vietnameseText(word.getVietnameseText())
                        .imageUrl(word.getImageUrl())
                        .audioUrl(word.getAudioUrl());
            }

            if (multipleChoiceExercise.getSentence() != null) {
                Sentence sentence = multipleChoiceExercise.getSentence();
                questionBuilder
                        .englishText(sentence.getEnglishText())
                        .vietnameseText(sentence.getVietnameseText())
                        .audioUrl(sentence.getAudioUrl());
            }

            List<MultipleChoiceExerciseResponse.Option> options = new ArrayList<>();
            List<MultipleChoiceOption> multipleChoiceOptions =
                    exercise.getMultipleChoiceExercise().getMultipleChoiceOptions();

            for (var o : multipleChoiceOptions) {

                if (o.getWord() != null && o.getSentence() != null) {
                    throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
                }

                var optionBuilder = MultipleChoiceExerciseResponse.Option.builder();

                if (o.getWord() != null) {
                    Word word = o.getWord();
                    optionBuilder
                            .optionType(o.getOptionType())
                            .isCorrect(o.isCorrect())
                            .englishText(word.getEnglishText())
                            .vietnameseText(word.getVietnameseText())
                            .imageUrl(word.getImageUrl())
                            .audioUrl(word.getAudioUrl());
                }

                if (o.getSentence() != null) {
                    Sentence sentence = o.getSentence();
                    optionBuilder
                            .optionType(o.getOptionType())
                            .isCorrect(o.isCorrect())
                            .englishText(sentence.getEnglishText())
                            .vietnameseText(sentence.getVietnameseText())
                            .audioUrl(sentence.getAudioUrl());
                }

                options.add(optionBuilder.build());
            }


            MultipleChoiceExerciseResponse multipleChoiceExerciseResponse = MultipleChoiceExerciseResponse.builder()
                    .sourceLanguage(multipleChoiceExercise.getSourceLanguage())
                    .targetLanguage(multipleChoiceExercise.getTargetLanguage())
                    .questionType(multipleChoiceExercise.getQuestionType())
                    .question(questionBuilder.build())
                    .options(options)
                    .build();

            return ExerciseDetailResponse.<MultipleChoiceExerciseResponse>builder()
                    .id(exercise.getId())
                    .instruction(exercise.getInstruction())
                    .displayOrder(exercise.getDisplayOrder())
                    .data(multipleChoiceExerciseResponse)
                    .build();
        }

        /**
         * Build a response for a matching exercise.
         *
         * @param exercise the exercise entity
         * @return the response for the matching exercise
         */
        private ExerciseDetailResponse<List<MatchingExerciseResponse>> buildMatchingExerciseResponse(Exercise exercise) {
            List<MatchingExerciseResponse> matchingExerciseResponses = new ArrayList<>();
            List<MatchingPair> matchingPairs = exercise.getMatchingExercise().getMatchingPairs();

            for (var p : matchingPairs) {
                MatchingExerciseResponse matchingExerciseResponse = MatchingExerciseResponse.builder()
                        .id(p.getId())
                        .englishText(p.getWord().getEnglishText())
                        .vietnameseText(p.getWord().getVietnameseText())
                        .imageUrl(p.getWord().getImageUrl())
                        .audioUrl(p.getWord().getAudioUrl())
                        .build();
                matchingExerciseResponses.add(matchingExerciseResponse);
            }

            return ExerciseDetailResponse.<List<MatchingExerciseResponse>>builder()
                    .id(exercise.getId())
                    .instruction(exercise.getInstruction())
                    .displayOrder(exercise.getDisplayOrder())
                    .data(matchingExerciseResponses)
                    .build();
        }

        /**
         * Build a response for a word arrangement exercise.
         *
         * @param exercise the exercise entity
         * @return the response for the word arrangement exercise
         */
        private ExerciseDetailResponse<WordArrangementExerciseResponse> buildWordArrangementExerciseResponse(Exercise exercise) {
            WordArrangementExercise wordArrangementExercise = exercise.getWordArrangementExercise();

            // Build sentence response
            Sentence sentence = wordArrangementExercise.getSentence();

            if (sentence == null) {
                throw new ResourceNotFoundException(ErrorCodeEnum.RELATED_SENTENCES_NOT_EXISTED);
            }

            var sentenceResponse = WordArrangementExerciseResponse.SentenceResponse.builder()
                    .englishText(sentence.getEnglishText())
                    .vietnameseText(sentence.getVietnameseText())
                    .audioUrl(sentence.getAudioUrl())
                    .build();

            // Build words
            List<WordArrangementOption> wordArrangementOptions = wordArrangementExercise.getWordArrangementOptions();
            List<WordArrangementExerciseResponse.WordResponse> wordResponses = new ArrayList<>();
            for (WordArrangementOption w : wordArrangementOptions) {
                WordArrangementExerciseResponse.WordResponse wordResponse = WordArrangementExerciseResponse.WordResponse.builder()
                        .text(w.getWordText())
                        .isDistractor(w.isDistractor())
                        .correctPosition(w.getCorrectPosition())
                        .build();
                wordResponses.add(wordResponse);
            }

            WordArrangementExerciseResponse wordArrangementExerciseResponse = WordArrangementExerciseResponse.builder()
                    .sourceLanguage(wordArrangementExercise.getSourceLanguage())
                    .targetLanguage(wordArrangementExercise.getTargetLanguage())
                    .sentence(sentenceResponse)
                    .words(wordResponses)
                    .build();

            return ExerciseDetailResponse.<WordArrangementExerciseResponse>builder()
                    .id(exercise.getId())
                    .instruction(exercise.getInstruction())
                    .displayOrder(exercise.getDisplayOrder())
                    .data(wordArrangementExerciseResponse)
                    .build();
        }

        /**
         * Build a response for a dialogue exercise.
         *
         * @param exercise the exercise entity
         * @return the response for the dialogue exercise
         */
        private ExerciseDetailResponse<DialogueExerciseResponse> buildDialogueExerciseResponse(Exercise exercise) {
            DialogueExercise dialogueExercise = exercise.getDialogueExercise();

            List<DialogueExerciseLine> dialogueExerciseLines = dialogueExercise.getDialogueExerciseLines();
            List<DialogueExerciseResponse.DialogueLineResponse> dialogueLineResponses = new ArrayList<>();
            for (DialogueExerciseLine d : dialogueExerciseLines) {
                DialogueExerciseResponse.DialogueLineResponse.DialogueLineResponseBuilder builder = DialogueExerciseResponse.DialogueLineResponse.builder();

                builder.isChangeSpeaker(d.getSpeaker().equals(SpeakerEnum.A)); // true if speaker is A, false when speaker is B

                // handler english blank text
                if (d.isHasBlank()) {
                    String blankWord = d.getBlankWord();
                    String blankEnglishText = d.getEnglishText().replace(blankWord, "[]");
                    builder.englishText(blankEnglishText);
                } else {
                    builder.englishText(d.getEnglishText());
                }

                DialogueExerciseResponse.DialogueLineResponse dialogueLineResponse = builder
                        .vietnameseText(d.getVietnameseText())
                        .audioUrl(d.getAudioUrl())
                        .displayOrder(d.getDisplayOrder())
                        .blankWord(d.getBlankWord())
                        .build();
                dialogueLineResponses.add(dialogueLineResponse);
            }


            DialogueExerciseResponse dialogueExerciseResponse = DialogueExerciseResponse.builder()
                    .context(dialogueExercise.getContext())
                    .dialogueExerciseLines(dialogueLineResponses)
                    .build();


            return ExerciseDetailResponse.<DialogueExerciseResponse>builder()
                    .id(exercise.getId())
                    .instruction(exercise.getInstruction())
                    .displayOrder(exercise.getDisplayOrder())
                    .data(dialogueExerciseResponse)
                    .build();
        }

        /**
         * Build a response for a speaking exercise.
         *
         * @param topicId the id of the topic
         * @param exerciseDetails the list of exercise details to add to
         */
        private void buildSpeakingExerciseResponse(long topicId, List<Object> exerciseDetails) {
            // Find all the related words and topics from this topic
            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.TOPIC_NOT_EXISTED));

            // Get all words and sentences as individual items
            List<Word> words = topic.getWords();
            List<Sentence> sentences = topic.getSentences();

            // Combine words and sentences into a single collection
            List<Object> speakingMaterials = new ArrayList<>();
            speakingMaterials.addAll(words);
            speakingMaterials.addAll(sentences);

            // Shuffle to randomize the order
            Collections.shuffle(speakingMaterials);

            // Take at most 6 items or all available if less
            int itemsToTake = Math.min(6, speakingMaterials.size());
            List<Object> selectedMaterials = speakingMaterials.subList(0, itemsToTake);

            // Build speaking exercise responses from selected materials
            for (Object material : selectedMaterials) {
                if (material instanceof Word w) {
                    SpeakingExerciseResponse speakingExerciseResponse = SpeakingExerciseResponse.builder()
                            .englishText(w.getEnglishText())
                            .vietnameseText(w.getVietnameseText())
                            .audioUrl(w.getAudioUrl())
                            .build();
                    exerciseDetails.add(speakingExerciseResponse);
                } else if (material instanceof Sentence s) {
                    SpeakingExerciseResponse speakingExerciseResponse = SpeakingExerciseResponse.builder()
                            .englishText(s.getEnglishText())
                            .vietnameseText(s.getVietnameseText())
                            .audioUrl(s.getAudioUrl())
                            .build();
                    exerciseDetails.add(speakingExerciseResponse);
                }
            }
        }

        /**
         * Build exercises for an exam lesson.
         *
         * @param topicId the id of the topic
         * @param exerciseDetails the list of exercise details to add to
         */
        private void buildExercisesForExamLesson(long topicId, List<Object> exerciseDetails) {
            // Find all the related exercises from lessons which have type == 1 (core lesson)
            List<Lesson> coreLessons = lessonRepository.findByTopicIdAndLessonTypeId(topicId, 1L);
            List<Exercise> coreExercises = new ArrayList<>();
            for (Lesson lesson : coreLessons) {
                coreExercises.addAll(exerciseRepository.findByLessonId(lesson.getId()));
            }

            // add exercises to the exercise detail list
            // First, separate exercises by type
            List<Exercise> multipleChoiceExercises = new ArrayList<>();
            List<Exercise> wordArrangementExercises = new ArrayList<>();

            for (Exercise exercise : coreExercises) {
                if (exercise.getMultipleChoiceExercise() != null) {
                    multipleChoiceExercises.add(exercise);
                } else if (exercise.getWordArrangementExercise() != null) {
                    wordArrangementExercises.add(exercise);
                }
            }

            // Randomize both lists
            Collections.shuffle(multipleChoiceExercises);
            Collections.shuffle(wordArrangementExercises);

            // Take up to 15 exercises of each type (15 total)
            int mcCount = Math.min(8, multipleChoiceExercises.size());
            int waCount = Math.min(7, wordArrangementExercises.size());

            // Add them to exerciseDetails in alternating order
            // Track current display order to ensure ascending order
            int currentDisplayOrder = 1;
            for (int i = 0; i < Math.max(mcCount, waCount); i++) {
                if (i < mcCount) {
                    multipleChoiceExercises.get(i).setDisplayOrder(currentDisplayOrder);
                    exerciseDetails.add(buildMultipleChoiceExerciseResponse(multipleChoiceExercises.get(i)));
                    currentDisplayOrder++;
                }
                if (i < waCount) {
                    wordArrangementExercises.get(i).setDisplayOrder(currentDisplayOrder);
                    exerciseDetails.add(buildWordArrangementExerciseResponse(wordArrangementExercises.get(i)));
                    currentDisplayOrder++;
                }
            }
        }
    }
