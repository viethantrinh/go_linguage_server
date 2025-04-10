package tech.trvihnls.features.excercise.services;

import tech.trvihnls.features.excercise.dtos.request.admin.VocabularyExerciseCreateRequest;
import tech.trvihnls.features.excercise.dtos.request.admin.VocabularyExerciseUpdateRequest;
import tech.trvihnls.features.excercise.dtos.response.admin.VocabularyExerciseDetailResponse;

public interface ExerciseService {
    void createVocabularyExercise(VocabularyExerciseCreateRequest request);
    VocabularyExerciseDetailResponse getVocabularyExerciseDetail(Long exerciseId);
    void updateVocabularyExercise(VocabularyExerciseUpdateRequest request);
}
