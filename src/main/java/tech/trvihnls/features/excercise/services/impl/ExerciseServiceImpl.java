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
import tech.trvihnls.features.excercise.dtos.request.admin.*;
import tech.trvihnls.features.excercise.dtos.response.admin.MultipleChoiceExerciseDetailResponse;
import tech.trvihnls.features.excercise.dtos.response.admin.MultipleChoiceOptionDetailResponse;
import tech.trvihnls.features.excercise.dtos.response.admin.VocabularyExerciseDetailResponse;
import tech.trvihnls.features.excercise.services.ExerciseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {


    private final ExerciseRepository exerciseRepository;
    private final VocabularyExerciseRepository vocabularyExerciseRepository;
    private final WordRepository wordRepository;
    private final MultipleChoiceExerciseRepository multipleChoiceExerciseRepository;
    private final SentenceRepository sentenceRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private final MultipleChoiceOptionRepository multipleChoiceOptionRepository;

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


}
