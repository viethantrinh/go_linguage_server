package tech.trvihnls.features.excercise.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.trvihnls.commons.domains.*;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.*;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.commons.utils.enums.ExerciseTypeEnum;
import tech.trvihnls.commons.utils.enums.LanguageEnum;
import tech.trvihnls.commons.utils.enums.MultipleChoiceExerciseEnum;
import tech.trvihnls.features.ai.services.TtsService;
import tech.trvihnls.features.excercise.dtos.request.admin.*;
import tech.trvihnls.features.excercise.dtos.response.admin.*;
import tech.trvihnls.features.excercise.services.ExerciseService;
import tech.trvihnls.features.media.dtos.response.CloudinaryUrlResponse;
import tech.trvihnls.features.media.services.MediaUploadService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {


    private final ExerciseRepository exerciseRepository;
    private final VocabularyExerciseRepository vocabularyExerciseRepository;
    private final WordRepository wordRepository;
    private final MultipleChoiceExerciseRepository multipleChoiceExerciseRepository;
    private final SentenceRepository sentenceRepository;
    private final MultipleChoiceOptionRepository multipleChoiceOptionRepository;
    private final MatchingExerciseRepository matchingExerciseRepository;
    private final MatchingPairRepository matchingPairRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private final WordArrangementExerciseRepository wordArrangementExerciseRepository;
    private final WordArrangementOptionRepository wordArrangementOptionRepository;
    private final TtsService ttsService;
    private final MediaUploadService mediaUploadService;
    private final DialogueExerciseRepository dialogueExerciseRepository;
    private final DialogueExerciseLineRepository dialogueExerciseLineRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void createVocabularyExercise(VocabularyExerciseCreateRequest request) {
        Long exerciseId = request.getExerciseId();
        Long wordId = request.getWordId();

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.VOCABULARY_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        if (exercise.getVocabularyExercise() != null) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Create the composite key
        VocabularyExerciseId vocabularyExerciseId = new VocabularyExerciseId(wordId, exerciseId);

        // Create the vocabulary exercise with proper associations
        VocabularyExercise vocabularyExercise = new VocabularyExercise();
        vocabularyExercise.setId(vocabularyExerciseId);
        vocabularyExercise.setWord(word);
        vocabularyExercise.setExercise(exercise);

        // Set the bidirectional relationship
        exercise.setVocabularyExercise(vocabularyExercise);

        // Save the exercise first (which cascades to vocabulary exercise if configured)
        exerciseRepository.save(exercise);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public VocabularyExerciseDetailResponse getVocabularyExerciseDetail(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));


        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.VOCABULARY_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        VocabularyExerciseDetailResponse.VocabularyExerciseDetailResponseBuilder builder =
                VocabularyExerciseDetailResponse.builder();

        builder.exerciseId(exercise.getId());
        builder.exerciseName(exercise.getInstruction());

        if (exercise.getVocabularyExercise() != null) {
            builder.relatedWordId(exercise.getVocabularyExercise().getId().getWordId());
        } else {
            builder.relatedWordId(null);
        }

        return builder.build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void updateVocabularyExercise(VocabularyExerciseUpdateRequest request) {
        Long exerciseId = request.getExerciseId();
        Long wordId = request.getWordId();

        // Get required entities
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.VOCABULARY_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Check if no change needed
        if (exercise.getVocabularyExercise() != null &&
                exercise.getVocabularyExercise().getId().getWordId().equals(word.getId())) {
            return;
        }

        // Delete existing association directly with native SQL
        if (exercise.getVocabularyExercise() != null) {
            entityManager.createNativeQuery("DELETE FROM tbl_vocabulary_exercise WHERE exercise_id = :exerciseId")
                    .setParameter("exerciseId", exerciseId)
                    .executeUpdate();

            // Clear the persistence context to avoid stale references
            entityManager.flush();
            entityManager.clear();
        }

        // Re-fetch the exercise to get a fresh instance
        exercise = entityManager.find(Exercise.class, exerciseId);
        exercise.setVocabularyExercise(null);
        entityManager.merge(exercise);
        entityManager.flush();

        // Create new vocabulary exercise using native SQL
        String insertSql = "INSERT INTO tbl_vocabulary_exercise (word_id, exercise_id, created_at) VALUES (:wordId, :exerciseId, NOW())";
        entityManager.createNativeQuery(insertSql)
                .setParameter("wordId", wordId)
                .setParameter("exerciseId", exerciseId)
                .executeUpdate();

        entityManager.flush();
        entityManager.clear();

        // Re-fetch all entities to get fresh instances with correct relationships
        exercise = entityManager.find(Exercise.class, exerciseId);
        word = entityManager.find(Word.class, wordId);

        // Reconstruct the ID for fetching
        VocabularyExerciseId vocabularyExerciseId = new VocabularyExerciseId(wordId, exerciseId);
        VocabularyExercise vocabularyExercise = entityManager.find(VocabularyExercise.class, vocabularyExerciseId);

        // Set up proper relationships
        vocabularyExercise.setExercise(exercise);
        vocabularyExercise.setWord(word);
        exercise.setVocabularyExercise(vocabularyExercise);

        // Merge changes back
        entityManager.merge(vocabularyExercise);
        entityManager.merge(exercise);
        entityManager.flush();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public MultipleChoiceExerciseDetailResponse getMultipleChoiceExerciseDetailResponse(Long exerciseId) {
        MultipleChoiceExerciseDetailResponse.MultipleChoiceExerciseDetailResponseBuilder builder =
                MultipleChoiceExerciseDetailResponse.builder();

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!exercise.getExerciseType().getId().equals(ExerciseTypeEnum.MULTIPLE_CHOICE_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        MultipleChoiceExercise multipleChoiceExercise = multipleChoiceExerciseRepository.findByExerciseId(exerciseId)
                .orElse(null);

        if (multipleChoiceExercise == null) {
            return MultipleChoiceExerciseDetailResponse.builder().build();
        }


        builder.exerciseId(exercise.getId());
        builder.exerciseName(exercise.getInstruction());
        builder.questionType(multipleChoiceExercise.getQuestionType().name());
        builder.sourceLanguage(multipleChoiceExercise.getSourceLanguage().name());
        builder.targetLanguage(multipleChoiceExercise.getTargetLanguage().name());

        if (multipleChoiceExercise.getWord() == null && multipleChoiceExercise.getSentence() == null) {
            throw new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED);
        }

        if (multipleChoiceExercise.getWord() != null) {
            builder.relatedWordId(multipleChoiceExercise.getWord().getId());
            List<MultipleChoiceOption> multipleChoiceOptions = multipleChoiceExercise.getMultipleChoiceOptions();
            List<MultipleChoiceOptionDetailResponse> options = new ArrayList<>();
            for (MultipleChoiceOption mco : multipleChoiceOptions) {
                MultipleChoiceOptionDetailResponse mcodr = MultipleChoiceOptionDetailResponse.builder()
                        .id(mco.getId())
                        .optionType(mco.getOptionType().name())
                        .isCorrect(mco.isCorrect())
                        .wordId(mco.getWord().getId())
                        .word(
                                MultipleChoiceOptionDetailResponse.WordDetail.builder()
                                        .wordId(mco.getWord().getId())
                                        .englishText(mco.getWord().getEnglishText())
                                        .vietnameseText(mco.getWord().getVietnameseText())
                                        .build()
                        )
                        .build();

                options.add(mcodr);
            }
            builder.options(options);
        }

        if (multipleChoiceExercise.getSentence() != null) {
            builder.relatedSentenceId(multipleChoiceExercise.getSentence().getId());
            List<MultipleChoiceOption> multipleChoiceOptions = multipleChoiceExercise.getMultipleChoiceOptions();
            List<MultipleChoiceOptionDetailResponse> options = new ArrayList<>();
            for (MultipleChoiceOption mco : multipleChoiceOptions) {
                MultipleChoiceOptionDetailResponse mcodr = MultipleChoiceOptionDetailResponse.builder()
                        .id(mco.getId())
                        .optionType(mco.getOptionType().name())
                        .isCorrect(mco.isCorrect())
                        .sentenceId(mco.getSentence().getId())
                        .sentence(
                                MultipleChoiceOptionDetailResponse.SentenceDetail.builder()
                                        .sentenceId(mco.getSentence().getId())
                                        .englishText(mco.getSentence().getEnglishText())
                                        .vietnameseText(mco.getSentence().getVietnameseText())
                                        .build()
                        )
                        .build();
                options.add(mcodr);
            }
            builder.options(options);
        }

        return builder.build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void createMultipleChoiceExercise(MultipleChoiceExerciseCreateRequest request) {
        Long exerciseId = request.getExerciseId();

        // Convert string enums to actual enum values
        MultipleChoiceExerciseEnum questionType = MultipleChoiceExerciseEnum.valueOf(request.getQuestionType());
        LanguageEnum sourceLanguage = LanguageEnum.valueOf(request.getSourceLanguage());
        LanguageEnum targetLanguage = LanguageEnum.valueOf(request.getTargetLanguage());

        // Validate exercise exists and is of the correct type
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.MULTIPLE_CHOICE_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Check if a multiple choice exercise already exists for this exercise
        if (exercise.getMultipleChoiceExercise() != null) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Validate word or sentence based on questionType
        Word word = null;
        Sentence sentence = null;

        if (questionType == MultipleChoiceExerciseEnum.word) {
            if (request.getWordId() == null) {
                throw new AppException(ErrorCodeEnum.BAD_REQUEST);
            }
            word = wordRepository.findById(request.getWordId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        } else if (questionType == MultipleChoiceExerciseEnum.sentence ||
                questionType == MultipleChoiceExerciseEnum.audio) {
            if (request.getSentenceId() == null) {
                throw new AppException(ErrorCodeEnum.BAD_REQUEST);
            }
            sentence = sentenceRepository.findById(request.getSentenceId())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        }

        // Create multiple choice exercise
        MultipleChoiceExercise multipleChoiceExercise = MultipleChoiceExercise.builder()
                .questionType(questionType)
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .exercise(exercise)
                .word(word)
                .sentence(sentence)
                .build();

        // Set up bidirectional relationship
        exercise.setMultipleChoiceExercise(multipleChoiceExercise);

        // Save the multiple choice exercise
        MultipleChoiceExercise savedMultipleChoiceExercise = multipleChoiceExerciseRepository.save(multipleChoiceExercise);

        // Create and save options
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            for (MultipleChoiceOptionCreateRequest optionRequest : request.getOptions()) {
                MultipleChoiceExerciseEnum optionType = MultipleChoiceExerciseEnum.valueOf(optionRequest.getOptionType());
                Word optionWord = null;
                Sentence optionSentence = null;

                if (optionType == MultipleChoiceExerciseEnum.word) {
                    if (optionRequest.getWordId() == null) {
                        throw new AppException(ErrorCodeEnum.BAD_REQUEST);
                    }
                    optionWord = wordRepository.findById(optionRequest.getWordId())
                            .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
                } else if (optionType == MultipleChoiceExerciseEnum.sentence) {
                    if (optionRequest.getSentenceId() == null) {
                        throw new AppException(ErrorCodeEnum.BAD_REQUEST);
                    }
                    optionSentence = sentenceRepository.findById(optionRequest.getSentenceId())
                            .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
                }

                MultipleChoiceOption option = MultipleChoiceOption.builder()
                        .optionType(optionType)
                        .isCorrect(optionRequest.isCorrect())
                        .multipleChoiceExercise(savedMultipleChoiceExercise)
                        .word(optionWord)
                        .sentence(optionSentence)
                        .build();

                MultipleChoiceOption savedOption = multipleChoiceOptionRepository.save(option);
                savedMultipleChoiceExercise.getMultipleChoiceOptions().add(savedOption);
            }
        }

        // Save everything
        exerciseRepository.save(exercise);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void updateMultipleChoiceExercise(MultipleChoiceExerciseUpdateRequest request) {
        Long exerciseId = request.getExerciseId();

        // Validate exercise exists and is of the correct type
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.MULTIPLE_CHOICE_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Check if multiple choice exercise exists
        if (exercise.getMultipleChoiceExercise() == null) {
            throw new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED);
        }

        // Get the existing ID before deleting
        Long multipleChoiceExerciseId = exercise.getMultipleChoiceExercise().getId();

        // Delete all options first
        entityManager.createNativeQuery("DELETE FROM tbl_multiple_choice_option WHERE multiple_choice_exercise_id = :id")
                .setParameter("id", multipleChoiceExerciseId)
                .executeUpdate();

        // Delete the multiple choice exercise
        entityManager.createNativeQuery("DELETE FROM tbl_multiple_choice_exercise WHERE id = :id")
                .setParameter("id", multipleChoiceExerciseId)
                .executeUpdate();

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Re-fetch exercise
        exercise = entityManager.find(Exercise.class, exerciseId);
        exercise.setMultipleChoiceExercise(null);
        entityManager.merge(exercise);
        entityManager.flush();

        // Convert string enums to actual enum values
        MultipleChoiceExerciseEnum questionType = MultipleChoiceExerciseEnum.valueOf(request.getQuestionType().toLowerCase());
        LanguageEnum sourceLanguage = LanguageEnum.valueOf(request.getSourceLanguage().toLowerCase());
        LanguageEnum targetLanguage = LanguageEnum.valueOf(request.getTargetLanguage().toLowerCase());

        // Get word or sentence based on question type
        Long wordId = null;
        Long sentenceId = null;

        if (questionType == MultipleChoiceExerciseEnum.word) {
            if (request.getWordId() == null) {
                throw new AppException(ErrorCodeEnum.BAD_REQUEST);
            }
            wordId = request.getWordId();
            // Verify word exists
            wordRepository.findById(wordId)
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        } else if (questionType == MultipleChoiceExerciseEnum.sentence ||
                questionType == MultipleChoiceExerciseEnum.audio) {
            if (request.getSentenceId() == null) {
                throw new AppException(ErrorCodeEnum.BAD_REQUEST);
            }
            sentenceId = request.getSentenceId();
            // Verify sentence exists
            sentenceRepository.findById(sentenceId)
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
        }

        // Create new multiple choice exercise using native SQL
        String insertMceSql = "INSERT INTO tbl_multiple_choice_exercise " +
                "(question_type, source_language, target_language, exercise_id, word_id, sentence_id, created_at) " +
                "VALUES (:questionType, :sourceLanguage, :targetLanguage, :exerciseId, :wordId, :sentenceId, NOW())";

        entityManager.createNativeQuery(insertMceSql)
                .setParameter("questionType", questionType.name())
                .setParameter("sourceLanguage", sourceLanguage.name())
                .setParameter("targetLanguage", targetLanguage.name())
                .setParameter("exerciseId", exerciseId)
                .setParameter("wordId", wordId)
                .setParameter("sentenceId", sentenceId)
                .executeUpdate();

        entityManager.flush();

        // Get new multiple choice exercise
        MultipleChoiceExercise newMultipleChoiceExercise = multipleChoiceExerciseRepository.findByExerciseId(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // Create options
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            for (MultipleChoiceOptionUpdateRequest optionRequest : request.getOptions()) {
                MultipleChoiceExerciseEnum optionType = MultipleChoiceExerciseEnum.valueOf(optionRequest.getOptionType().toLowerCase());
                Long optionWordId = null;
                Long optionSentenceId = null;

                if (optionType == MultipleChoiceExerciseEnum.word) {
                    if (optionRequest.getWordId() == null) {
                        throw new AppException(ErrorCodeEnum.BAD_REQUEST);
                    }
                    optionWordId = optionRequest.getWordId();
                    // Verify word exists
                    wordRepository.findById(optionWordId)
                            .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
                } else if (optionType == MultipleChoiceExerciseEnum.sentence) {
                    if (optionRequest.getSentenceId() == null) {
                        throw new AppException(ErrorCodeEnum.BAD_REQUEST);
                    }
                    optionSentenceId = optionRequest.getSentenceId();
                    // Verify sentence exists
                    sentenceRepository.findById(optionSentenceId)
                            .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));
                }

                // Insert option
                String insertOptionSql = "INSERT INTO tbl_multiple_choice_option " +
                        "(option_type, is_correct, multiple_choice_exercise_id, word_id, sentence_id, created_at) " +
                        "VALUES (:optionType, :isCorrect, :mceId, :wordId, :sentenceId, NOW())";

                entityManager.createNativeQuery(insertOptionSql)
                        .setParameter("optionType", optionType.name())
                        .setParameter("isCorrect", optionRequest.isCorrect())
                        .setParameter("mceId", newMultipleChoiceExercise.getId())
                        .setParameter("wordId", optionWordId)
                        .setParameter("sentenceId", optionSentenceId)
                        .executeUpdate();
            }
        }

        entityManager.flush();
        entityManager.clear();

        // Re-fetch everything to ensure clean state
        exercise = entityManager.find(Exercise.class, exerciseId);
        MultipleChoiceExercise multipleChoiceExercise = entityManager.find(MultipleChoiceExercise.class,
                newMultipleChoiceExercise.getId());

        // Ensure proper relationships
        exercise.setMultipleChoiceExercise(multipleChoiceExercise);
        multipleChoiceExercise.setExercise(exercise);

        entityManager.merge(exercise);
        entityManager.merge(multipleChoiceExercise);
        entityManager.flush();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public MatchingExerciseDetailResponse getMatchingExerciseDetail(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.MATCHING_WORD_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        MatchingExercise matchingExercise = matchingExerciseRepository.findByExerciseId(exerciseId)
                .orElse(null);

        if (matchingExercise == null) {
            return MatchingExerciseDetailResponse.builder()
                    .build();
        }

        List<MatchingPairDetailResponse> pairResponses = matchingExercise.getMatchingPairs().stream()
                .map(pair -> {
                    Word word = pair.getWord();
                    return MatchingPairDetailResponse.builder()
                            .id(pair.getId())
                            .matchingExerciseId(matchingExercise.getId())
                            .word(MatchingPairDetailResponse.WordDetail.builder()
                                    .wordId(word.getId())
                                    .englishText(word.getEnglishText())
                                    .vietnameseText(word.getVietnameseText())
                                    .imageUrl(word.getImageUrl())
                                    .audioUrl(word.getAudioUrl())
                                    .build())
                            .build();
                })
                .collect(Collectors.toList());

        return MatchingExerciseDetailResponse.builder()
                .exerciseId(exercise.getId())
                .exerciseName(exercise.getInstruction())
                .matchingPairs(pairResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void createMatchingExercise(MatchingExerciseCreateRequest request) {
        Long exerciseId = request.getExerciseId();

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.MATCHING_WORD_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        if (exercise.getMatchingExercise() != null) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Create matching exercise
        MatchingExercise matchingExercise = MatchingExercise.builder()
                .exercise(exercise)
                .matchingPairs(new ArrayList<>())
                .build();

        MatchingExercise savedMatchingExercise = matchingExerciseRepository.save(matchingExercise);
        exercise.setMatchingExercise(savedMatchingExercise);

        // Create matching pairs
        if (request.getMatchingPairs() != null && !request.getMatchingPairs().isEmpty()) {
            for (MatchingPairCreateRequest pairRequest : request.getMatchingPairs()) {
                Word word = wordRepository.findById(pairRequest.getWordId())
                        .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

                MatchingPair matchingPair = MatchingPair.builder()
                        .matchingExercise(savedMatchingExercise)
                        .word(word)
                        .build();

                MatchingPair savedPair = matchingPairRepository.save(matchingPair);
                savedMatchingExercise.getMatchingPairs().add(savedPair);
            }
        }

        exerciseRepository.save(exercise);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void updateMatchingExercise(MatchingExerciseUpdateRequest request) {
        Long exerciseId = request.getExerciseId();

        // Validate exercise exists and is of correct type
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.MATCHING_WORD_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Check if matching exercise exists
        MatchingExercise matchingExercise = matchingExerciseRepository.findByExerciseId(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // Get the existing ID before deleting
        Long matchingExerciseId = matchingExercise.getId();

        // Delete all pairs first
        entityManager.createNativeQuery("DELETE FROM tbl_matching_pair WHERE matching_exercise_id = :id")
                .setParameter("id", matchingExerciseId)
                .executeUpdate();

        // Delete the matching exercise
        entityManager.createNativeQuery("DELETE FROM tbl_matching_exercise WHERE id = :id")
                .setParameter("id", matchingExerciseId)
                .executeUpdate();

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Re-fetch exercise
        exercise = entityManager.find(Exercise.class, exerciseId);
        exercise.setMatchingExercise(null);
        entityManager.merge(exercise);
        entityManager.flush();

        // Create new matching exercise using native SQL
        String insertSql = "INSERT INTO tbl_matching_exercise (exercise_id, created_at) VALUES (:exerciseId, NOW())";
        entityManager.createNativeQuery(insertSql)
                .setParameter("exerciseId", exerciseId)
                .executeUpdate();

        entityManager.flush();

        // Get new matching exercise
        MatchingExercise newMatchingExercise = matchingExerciseRepository.findByExerciseId(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // Create matching pairs
        if (request.getMatchingPairs() != null && !request.getMatchingPairs().isEmpty()) {
            for (MatchingPairUpdateRequest pairRequest : request.getMatchingPairs()) {
                // Verify word exists
                Long wordId = pairRequest.getWordId();
                if (!wordRepository.existsById(wordId)) {
                    throw new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED);
                }

                // Insert pair
                String insertPairSql = "INSERT INTO tbl_matching_pair (matching_exercise_id, word_id, created_at) " +
                        "VALUES (:matchingExerciseId, :wordId, NOW())";
                entityManager.createNativeQuery(insertPairSql)
                        .setParameter("matchingExerciseId", newMatchingExercise.getId())
                        .setParameter("wordId", wordId)
                        .executeUpdate();
            }
        }

        entityManager.flush();
        entityManager.clear();

        // Re-fetch everything to ensure clean state
        exercise = entityManager.find(Exercise.class, exerciseId);
        MatchingExercise refreshedMatchingExercise = entityManager.find(MatchingExercise.class,
                newMatchingExercise.getId());

        // Ensure proper relationships
        exercise.setMatchingExercise(refreshedMatchingExercise);
        refreshedMatchingExercise.setExercise(exercise);

        entityManager.merge(exercise);
        entityManager.merge(refreshedMatchingExercise);
        entityManager.flush();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public WordArrangementExerciseDetailResponse getWordArrangementExerciseDetail(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.WORD_ARRANGEMENT_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        WordArrangementExercise wordArrangementExercise = exercise.getWordArrangementExercise();
        if (wordArrangementExercise == null) {
            return WordArrangementExerciseDetailResponse.builder()
                    .build();
        }

        WordArrangementExerciseDetailResponse.WordArrangementExerciseDetailResponseBuilder builder =
                WordArrangementExerciseDetailResponse.builder()
                        .exerciseId(exercise.getId())
                        .exerciseName(exercise.getInstruction())
                        .sourceLanguage(wordArrangementExercise.getSourceLanguage().name())
                        .targetLanguage(wordArrangementExercise.getTargetLanguage().name());

        if (wordArrangementExercise.getSentence() != null) {
            Sentence sentence = wordArrangementExercise.getSentence();
            builder.sentenceId(sentence.getId());
            builder.sentence(WordArrangementExerciseDetailResponse.SentenceDetail.builder()
                    .sentenceId(sentence.getId())
                    .englishText(sentence.getEnglishText())
                    .vietnameseText(sentence.getVietnameseText())
                    .audioUrl(sentence.getAudioUrl())
                    .build());
        }

        List<WordArrangementOptionDetailResponse> optionResponses = wordArrangementExercise.getWordArrangementOptions().stream()
                .map(option -> WordArrangementOptionDetailResponse.builder()
                        .id(option.getId())
                        .wordArrangementExerciseId(wordArrangementExercise.getId())
                        .wordText(option.getWordText())
                        .isDistractor(option.isDistractor())
                        .correctPosition(option.getCorrectPosition())
                        .build())
                .collect(Collectors.toList());

        builder.options(optionResponses);

        return builder.build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void createWordArrangementExercise(WordArrangementExerciseCreateRequest request) {
        Long exerciseId = request.getExerciseId();
        Long sentenceId = request.getSentenceId();

        // Validate exercise exists and is of correct type
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.WORD_ARRANGEMENT_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Check if word arrangement exercise already exists
        if (exercise.getWordArrangementExercise() != null) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Get sentence
        Sentence sentence = sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // Convert string enums to actual enum values
        LanguageEnum sourceLanguage = LanguageEnum.valueOf(request.getSourceLanguage().toLowerCase());
        LanguageEnum targetLanguage = LanguageEnum.valueOf(request.getTargetLanguage().toLowerCase());

        // Create word arrangement exercise
        WordArrangementExercise wordArrangementExercise = WordArrangementExercise.builder()
                .sourceLanguage(sourceLanguage)
                .targetLanguage(targetLanguage)
                .exercise(exercise)
                .sentence(sentence)
                .wordArrangementOptions(new ArrayList<>())
                .build();

        // Set up bidirectional relationship
        exercise.setWordArrangementExercise(wordArrangementExercise);

        // Save the exercise
        WordArrangementExercise savedExercise = wordArrangementExerciseRepository.save(wordArrangementExercise);

        // Create options
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            for (WordArrangementOptionCreateRequest optionRequest : request.getOptions()) {
                WordArrangementOption option = WordArrangementOption.builder()
                        .wordText(optionRequest.getWordText())
                        .isDistractor(optionRequest.isDistractor())
                        .correctPosition(optionRequest.getCorrectPosition())
                        .wordArrangementExercise(savedExercise)
                        .build();

                WordArrangementOption savedOption = wordArrangementOptionRepository.save(option);
                savedExercise.getWordArrangementOptions().add(savedOption);
            }
        }

        exerciseRepository.save(exercise);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void updateWordArrangementExercise(WordArrangementExerciseUpdateRequest request) {
        Long exerciseId = request.getExerciseId();

        // Validate exercise exists and is of correct type
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.WORD_ARRANGEMENT_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Check if word arrangement exercise exists
        if (exercise.getWordArrangementExercise() == null) {
            throw new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED);
        }

        // Get the existing ID before deleting
        Long wordArrangementExerciseId = exercise.getWordArrangementExercise().getId();

        // Delete all options first
        entityManager.createNativeQuery("DELETE FROM tbl_word_arrangement_option WHERE word_arrangement_exercise_id = :id")
                .setParameter("id", wordArrangementExerciseId)
                .executeUpdate();

        // Delete the word arrangement exercise
        entityManager.createNativeQuery("DELETE FROM tbl_word_arrangement_exercise WHERE id = :id")
                .setParameter("id", wordArrangementExerciseId)
                .executeUpdate();

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Re-fetch exercise
        exercise = entityManager.find(Exercise.class, exerciseId);
        exercise.setWordArrangementExercise(null);
        entityManager.merge(exercise);
        entityManager.flush();

        // Get sentence
        Long sentenceId = request.getSentenceId();
        // Verify sentence exists
        sentenceRepository.findById(sentenceId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // Convert string enums to actual enum values
        LanguageEnum sourceLanguage = LanguageEnum.valueOf(request.getSourceLanguage().toLowerCase());
        LanguageEnum targetLanguage = LanguageEnum.valueOf(request.getTargetLanguage().toLowerCase());

        // Create new word arrangement exercise using native SQL
        String insertSql = "INSERT INTO tbl_word_arrangement_exercise " +
                "(source_language, target_language, exercise_id, sentence_id, created_at) " +
                "VALUES (:sourceLanguage, :targetLanguage, :exerciseId, :sentenceId, NOW())";

        entityManager.createNativeQuery(insertSql)
                .setParameter("sourceLanguage", sourceLanguage.name())
                .setParameter("targetLanguage", targetLanguage.name())
                .setParameter("exerciseId", exerciseId)
                .setParameter("sentenceId", sentenceId)
                .executeUpdate();

        entityManager.flush();

        // Get the newly created word arrangement exercise
        WordArrangementExercise newExercise = wordArrangementExerciseRepository.findById(
                        wordArrangementExerciseRepository.findByExerciseId(exerciseId)
                                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED))
                                .getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // Create options
        if (request.getOptions() != null && !request.getOptions().isEmpty()) {
            for (WordArrangementOptionUpdateRequest optionRequest : request.getOptions()) {
                String optionSql = "INSERT INTO tbl_word_arrangement_option " +
                        "(word_text, is_distractor, correct_position, word_arrangement_exercise_id, created_at) " +
                        "VALUES (:wordText, :isDistractor, :correctPosition, :exerciseId, NOW())";

                entityManager.createNativeQuery(optionSql)
                        .setParameter("wordText", optionRequest.getWordText())
                        .setParameter("isDistractor", optionRequest.isDistractor())
                        .setParameter("correctPosition", optionRequest.getCorrectPosition() != null ? optionRequest.getCorrectPosition() : -1)
                        .setParameter("exerciseId", newExercise.getId())
                        .executeUpdate();
            }
        }

        entityManager.flush();
        entityManager.clear();

        // Re-fetch everything to ensure clean state
        exercise = entityManager.find(Exercise.class, exerciseId);
        WordArrangementExercise refreshedExercise = entityManager.find(WordArrangementExercise.class,
                newExercise.getId());

        // Ensure proper relationships
        exercise.setWordArrangementExercise(refreshedExercise);
        refreshedExercise.setExercise(exercise);

        entityManager.merge(exercise);
        entityManager.merge(refreshedExercise);
        entityManager.flush();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public DialogueExerciseDetailResponse getDialogueExerciseDetailResponse(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.DIALOGUE_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        DialogueExercise dialogueExercise = exercise.getDialogueExercise();
        if (dialogueExercise == null) {
            return DialogueExerciseDetailResponse.builder().build();
        }

        List<DialogueExerciseDetailLineResponse> lineResponses = dialogueExercise.getDialogueExerciseLines().stream()
                .map(line -> DialogueExerciseDetailLineResponse.builder()
                        .id(line.getId())
                        .dialogueExerciseId(dialogueExercise.getId())
                        .speaker(line.getSpeaker())
                        .englishText(line.getEnglishText())
                        .vietnameseText(line.getVietnameseText())
                        .audioUrl(line.getAudioUrl())
                        .displayOrder(line.getDisplayOrder())
                        .hasBlank(line.isHasBlank())
                        .blankWord(line.getBlankWord())
                        .build())
                .collect(Collectors.toList());

        return DialogueExerciseDetailResponse.builder()
                .id(dialogueExercise.getId())
                .context(dialogueExercise.getContext())
                .exerciseId(exercise.getId())
                .dialogueLines(lineResponses)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void createDialogueExercise(DialogueExerciseCreateRequest request) {
        Long exerciseId = request.getExerciseId();

        // Validate exercise exists and is of the correct type
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.DIALOGUE_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Check if a dialogue exercise already exists for this exercise
        if (exercise.getDialogueExercise() != null) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Create dialogue exercise
        DialogueExercise dialogueExercise = DialogueExercise.builder()
                .context(request.getContext())
                .exercise(exercise)
                .dialogueExerciseLines(new ArrayList<>())
                .build();

        // Set up bidirectional relationship
        exercise.setDialogueExercise(dialogueExercise);

        // Save the dialogue exercise using its repository
        DialogueExercise savedDialogueExercise = dialogueExerciseRepository.save(dialogueExercise);

        // Create and save dialogue lines
        if (request.getDialogueLines() != null && !request.getDialogueLines().isEmpty()) {
            List<DialogueExerciseLine> lines = new ArrayList<>();

            for (DialogueExerciseLineCreateRequest lineRequest : request.getDialogueLines()) {
                // Using tts service from eleven lab to get the audio file from english text
                byte[] audioBytes = ttsService.requestTextToSpeech(lineRequest.getEnglishText());

                // Upload the audio file to cloudinary
                CloudinaryUrlResponse cloudinaryUrlResponse = mediaUploadService.uploadAudio(audioBytes);
                String audioUrl = cloudinaryUrlResponse.getSecureUrl();

                DialogueExerciseLine line = DialogueExerciseLine.builder()
                        .speaker(lineRequest.getSpeaker())
                        .englishText(lineRequest.getEnglishText())
                        .vietnameseText(lineRequest.getVietnameseText())
                        .displayOrder(Integer.parseInt(lineRequest.getDisplayOrder()))
                        .hasBlank(lineRequest.isHasBlank())
                        .blankWord(lineRequest.getBlankWord())
                        .dialogueExercise(savedDialogueExercise)
                        // TODO: triển khai upload lên cloud sau, tạm thời như này đỡ tốn tài nguyên - đã xong
                        .audioUrl(audioUrl) // Temporary audio URL
                        .build();

                lines.add(line);
            }

            // Save all lines at once
            dialogueExerciseLineRepository.saveAll(lines);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void updateDialogueExercise(DialogueExerciseUpdateRequest request) {
        Long exerciseId = request.getExerciseId();

        // Validate exercise exists and is of the correct type
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        if (!Objects.equals(exercise.getExerciseType().getId(), ExerciseTypeEnum.DIALOGUE_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        // Check if dialogue exercise exists
        DialogueExercise dialogueExercise = exercise.getDialogueExercise();
        if (dialogueExercise == null) {
            throw new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED);
        }

        // Store existing dialogue lines for comparison before deletion
        List<DialogueExerciseLine> existingLines = dialogueExercise.getDialogueExerciseLines();
        // Create a map of english text to audio URL for quick lookup
        Map<String, String> existingAudioMap = existingLines.stream()
                .collect(Collectors.toMap(DialogueExerciseLine::getEnglishText, DialogueExerciseLine::getAudioUrl, (a, b) -> a));

        // Get the existing ID before deleting
        Long dialogueExerciseId = dialogueExercise.getId();

        // Delete all lines first
        entityManager.createNativeQuery("DELETE FROM tbl_dialogue_exercise_line WHERE dialogue_exercise_id = :id")
                .setParameter("id", dialogueExerciseId)
                .executeUpdate();

        // Delete the dialogue exercise
        entityManager.createNativeQuery("DELETE FROM tbl_dialogue_exercise WHERE id = :id")
                .setParameter("id", dialogueExerciseId)
                .executeUpdate();

        // Clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Re-fetch exercise
        exercise = entityManager.find(Exercise.class, exerciseId);
        exercise.setDialogueExercise(null);
        entityManager.merge(exercise);
        entityManager.flush();

        // Create new dialogue exercise using native SQL
        String insertSql = "INSERT INTO tbl_dialogue_exercise (context, exercise_id, created_at) VALUES (:context, :exerciseId, NOW())";
        entityManager.createNativeQuery(insertSql)
                .setParameter("context", request.getContext())
                .setParameter("exerciseId", exerciseId)
                .executeUpdate();

        entityManager.flush();

        // Get new dialogue exercise
        DialogueExercise newDialogueExercise = dialogueExerciseRepository.findById(
                        dialogueExerciseRepository.findByExerciseIdOrderByIdDesc(exerciseId)
                                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED))
                                .getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCodeEnum.LEARNING_MATERIAL_NOT_EXISTED));

        // Create dialogue lines
        if (request.getDialogueLines() != null && !request.getDialogueLines().isEmpty()) {
            for (DialogueExerciseLineUpdateRequest lineRequest : request.getDialogueLines()) {
                // TODO: đẩy lên cloud = đã xong
                String englishText = lineRequest.getEnglishText();
                String audioUrl;

                // Check if the English text existed in the previous version and reuse the audio URL if unchanged
                if (existingAudioMap.containsKey(englishText) && !existingAudioMap.get(englishText).equals("SAMPLE_AUDIO_URL")) {
                    audioUrl = existingAudioMap.get(englishText);
                } else {
                    // Generate new audio for the English text
                    try {
                        byte[] audioBytes = ttsService.requestTextToSpeech(englishText);
                        CloudinaryUrlResponse cloudinaryUrlResponse = mediaUploadService.uploadAudio(audioBytes);
                        audioUrl = cloudinaryUrlResponse.getSecureUrl();
                    } catch (Exception e) {
                        // If there's any error during TTS or upload, fall back to sample URL
                        audioUrl = "SAMPLE_AUDIO_URL";
                    }
                }

                String insertLineSql = "INSERT INTO tbl_dialogue_exercise_line " +
                        "(speaker, english_text, vietnamese_text, audio_url, display_order, has_blank, blank_word, dialogue_exercise_id, created_at) " +
                        "VALUES (:speaker, :englishText, :vietnameseText, :audioUrl, :displayOrder, :hasBlank, :blankWord, :dialogueExerciseId, NOW())";


                entityManager.createNativeQuery(insertLineSql)
                        .setParameter("speaker", lineRequest.getSpeaker().name())
                        .setParameter("englishText", englishText)
                        .setParameter("vietnameseText", lineRequest.getVietnameseText())
                        //  (check trước đó xem english text request có khác thằng đang có trong database không)
                        .setParameter("audioUrl", audioUrl)  // Temporary audio URL
                        .setParameter("displayOrder", Integer.parseInt(lineRequest.getDisplayOrder()))
                        .setParameter("hasBlank", lineRequest.isHasBlank())
                        .setParameter("blankWord", lineRequest.getBlankWord())
                        .setParameter("dialogueExerciseId", newDialogueExercise.getId())
                        .executeUpdate();
            }
        }

        entityManager.flush();
        entityManager.clear();

        // Re-fetch everything to ensure clean state
        exercise = entityManager.find(Exercise.class, exerciseId);
        DialogueExercise refreshedDialogueExercise = entityManager.find(DialogueExercise.class, newDialogueExercise.getId());

        // Ensure proper relationships
        exercise.setDialogueExercise(refreshedDialogueExercise);
        refreshedDialogueExercise.setExercise(exercise);

        entityManager.merge(exercise);
        entityManager.merge(refreshedDialogueExercise);
        entityManager.flush();
    }
}
