package tech.trvihnls.features.word.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Exercise;
import tech.trvihnls.commons.repositories.ExerciseRepository;
import tech.trvihnls.commons.repositories.VocabularyExerciseRepository;
import tech.trvihnls.commons.repositories.WordRepository;
import tech.trvihnls.features.word.dtos.query.WordQuery;
import tech.trvihnls.features.word.dtos.response.admin.WordForVocabularyExerciseResponse;
import tech.trvihnls.features.word.services.WordService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final ExerciseRepository exerciseRepository;
    private final WordRepository wordRepository;
    private final VocabularyExerciseRepository vocabularyExerciseRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<WordForVocabularyExerciseResponse> getWordsByVocabularyExerciseId(Long vocabularyExerciseId) {
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(vocabularyExerciseId);

        if (exerciseOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Exercise exercise = exerciseOpt.get();
        List<WordQuery> words = wordRepository.findByExerciseId(exercise.getId());

        Set<Long> wordIDs = vocabularyExerciseRepository.findAllWordsID();

        List<WordForVocabularyExerciseResponse> wordForVocabularyExerciseResponses = new ArrayList<>();

        for (WordQuery word : words) {
            WordForVocabularyExerciseResponse builtWord = WordForVocabularyExerciseResponse.builder()
                    .wordId(word.getId())
                    .englishText(word.getEnglishText())
                    .vietnameseText(word.getVietnameseText())
                    .imageUrl(word.getImageUrl())
                    .audioUrl(word.getAudioUrl())
                    .isSelectedByAnotherVocabularyExercise(false)
                    .build();

            for (Long wordID : wordIDs) {
                if (builtWord.getWordId() == wordID) {
                    builtWord.setSelectedByAnotherVocabularyExercise(true);
                }
            }

            wordForVocabularyExerciseResponses.add(builtWord);
        }

        return wordForVocabularyExerciseResponses;
    }
}
