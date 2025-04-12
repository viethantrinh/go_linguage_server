package tech.trvihnls.features.excercise.services;

import tech.trvihnls.features.excercise.dtos.request.admin.MultipleChoiceExerciseCreateRequest;
import tech.trvihnls.features.excercise.dtos.request.admin.MultipleChoiceExerciseUpdateRequest;
import tech.trvihnls.features.excercise.dtos.request.admin.VocabularyExerciseCreateRequest;
import tech.trvihnls.features.excercise.dtos.request.admin.VocabularyExerciseUpdateRequest;
import tech.trvihnls.features.excercise.dtos.response.admin.MultipleChoiceExerciseDetailResponse;
import tech.trvihnls.features.excercise.dtos.response.admin.VocabularyExerciseDetailResponse;

public interface ExerciseService {
    void createVocabularyExercise(VocabularyExerciseCreateRequest request);
    VocabularyExerciseDetailResponse getVocabularyExerciseDetail(Long exerciseId);
    void updateVocabularyExercise(VocabularyExerciseUpdateRequest request);

    MultipleChoiceExerciseDetailResponse getMultipleChoiceExerciseDetailResponse(Long exerciseId);
    void createMultipleChoiceExercise(MultipleChoiceExerciseCreateRequest request);
    void updateMultipleChoiceExercise(MultipleChoiceExerciseUpdateRequest request);
}
