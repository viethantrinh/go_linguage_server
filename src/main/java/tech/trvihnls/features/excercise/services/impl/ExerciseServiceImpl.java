package tech.trvihnls.features.excercise.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.trvihnls.commons.domains.Exercise;
import tech.trvihnls.commons.domains.VocabularyExercise;
import tech.trvihnls.commons.domains.VocabularyExerciseId;
import tech.trvihnls.commons.domains.Word;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.exceptions.ResourceNotFoundException;
import tech.trvihnls.commons.repositories.ExerciseRepository;
import tech.trvihnls.commons.repositories.VocabularyExerciseRepository;
import tech.trvihnls.commons.repositories.WordRepository;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.commons.utils.enums.ExerciseTypeEnum;
import tech.trvihnls.features.excercise.dtos.request.admin.VocabularyExerciseCreateRequest;
import tech.trvihnls.features.excercise.dtos.request.admin.VocabularyExerciseUpdateRequest;
import tech.trvihnls.features.excercise.dtos.response.admin.VocabularyExerciseDetailResponse;
import tech.trvihnls.features.excercise.services.ExerciseService;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {


    private final ExerciseRepository exerciseRepository;
    private final VocabularyExerciseRepository vocabularyExerciseRepository;
    private final WordRepository wordRepository;
    @PersistenceContext
    private final EntityManager entityManager;

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
}
