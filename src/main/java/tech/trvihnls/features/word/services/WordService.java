package tech.trvihnls.features.word.services;

import tech.trvihnls.features.word.dtos.response.admin.WordForVocabularyExerciseResponse;

import java.util.List;

public interface WordService {
    List<WordForVocabularyExerciseResponse> getWordsByVocabularyExerciseId(Long vocabularyExerciseId);

}
