package tech.trvihnls.features.excercise.services;

import tech.trvihnls.features.excercise.dtos.request.admin.*;
import tech.trvihnls.features.excercise.dtos.response.admin.MatchingExerciseDetailResponse;
import tech.trvihnls.features.excercise.dtos.response.admin.MultipleChoiceExerciseDetailResponse;
import tech.trvihnls.features.excercise.dtos.response.admin.VocabularyExerciseDetailResponse;
import tech.trvihnls.features.excercise.dtos.response.admin.WordArrangementExerciseDetailResponse;

public interface ExerciseService {
    void createVocabularyExercise(VocabularyExerciseCreateRequest request);
    VocabularyExerciseDetailResponse getVocabularyExerciseDetail(Long exerciseId);
    void updateVocabularyExercise(VocabularyExerciseUpdateRequest request);

    MultipleChoiceExerciseDetailResponse getMultipleChoiceExerciseDetailResponse(Long exerciseId);
    void createMultipleChoiceExercise(MultipleChoiceExerciseCreateRequest request);
    void updateMultipleChoiceExercise(MultipleChoiceExerciseUpdateRequest request);

    MatchingExerciseDetailResponse getMatchingExerciseDetail(Long exerciseId);
    void createMatchingExercise(MatchingExerciseCreateRequest request);
    void updateMatchingExercise(MatchingExerciseUpdateRequest request);

    WordArrangementExerciseDetailResponse getWordArrangementExerciseDetail(Long exerciseId);
    void createWordArrangementExercise(WordArrangementExerciseCreateRequest request);
    void updateWordArrangementExercise(WordArrangementExerciseUpdateRequest request);
}
