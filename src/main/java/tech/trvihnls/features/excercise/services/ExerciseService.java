package tech.trvihnls.features.excercise.services;

import tech.trvihnls.features.excercise.dtos.request.admin.*;
import tech.trvihnls.features.excercise.dtos.response.admin.*;

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

    DialogueExerciseDetailResponse getDialogueExerciseDetailResponse(Long exerciseId);
    void createDialogueExercise(DialogueExerciseCreateRequest request);
    void updateDialogueExercise(DialogueExerciseUpdateRequest request);
}
