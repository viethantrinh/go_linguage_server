package tech.trvihnls.features.material.services;

import tech.trvihnls.features.material.dtos.response.admin.WordForMatchingExerciseResponse;
import tech.trvihnls.features.material.dtos.response.admin.WordForMultipleChoiceExerciseResponse;
import tech.trvihnls.features.material.dtos.response.admin.WordForVocabularyExerciseResponse;

import java.util.List;

public interface WordService {
    List<WordForVocabularyExerciseResponse> getWordsByVocabularyExerciseId(Long vocabularyExerciseId);
    List<WordForMultipleChoiceExerciseResponse> getWordsByMultipleChoiceExerciseId(Long exerciseId);
    List<WordForMatchingExerciseResponse> getWordsByMatchingExerciseId(Long exerciseId);
}
