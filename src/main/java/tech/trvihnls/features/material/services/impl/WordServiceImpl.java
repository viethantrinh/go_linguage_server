package tech.trvihnls.features.material.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import tech.trvihnls.commons.domains.Exercise;
import tech.trvihnls.commons.exceptions.AppException;
import tech.trvihnls.commons.repositories.*;
import tech.trvihnls.commons.utils.enums.ErrorCodeEnum;
import tech.trvihnls.commons.utils.enums.ExerciseTypeEnum;
import tech.trvihnls.features.material.dtos.query.WordQuery;
import tech.trvihnls.features.material.dtos.response.admin.WordForMatchingExerciseResponse;
import tech.trvihnls.features.material.dtos.response.admin.WordForMultipleChoiceExerciseResponse;
import tech.trvihnls.features.material.dtos.response.admin.WordForVocabularyExerciseResponse;
import tech.trvihnls.features.material.services.WordService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final ExerciseRepository exerciseRepository;
    private final WordRepository wordRepository;
    private final VocabularyExerciseRepository vocabularyExerciseRepository;
    private final MultipleChoiceExerciseRepository multipleChoiceExerciseRepository;
    private final MatchingExerciseRepository matchingExerciseRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<WordForVocabularyExerciseResponse> getWordsByVocabularyExerciseId(Long vocabularyExerciseId) {
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(vocabularyExerciseId);

        if (exerciseOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Exercise exercise = exerciseOpt.get();

        if (!exercise.getExerciseType().getId().equals(ExerciseTypeEnum.VOCABULARY_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

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

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<WordForMultipleChoiceExerciseResponse> getWordsByMultipleChoiceExerciseId(Long exerciseId) {
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseId);

        if (exerciseOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Exercise exercise = exerciseOpt.get();

        if (!exercise.getExerciseType().getId().equals(ExerciseTypeEnum.MULTIPLE_CHOICE_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        List<WordQuery> words = wordRepository.findByExerciseId(exercise.getId());

        Set<Long> wordIDs = multipleChoiceExerciseRepository.findAllWordsID();

        List<WordForMultipleChoiceExerciseResponse> wordForMultipleChoiceExerciseResponses = new ArrayList<>();

        for (WordQuery word : words) {
            WordForMultipleChoiceExerciseResponse builtWord = WordForMultipleChoiceExerciseResponse.builder()
                    .wordId(word.getId())
                    .englishText(word.getEnglishText())
                    .vietnameseText(word.getVietnameseText())
                    .imageUrl(word.getImageUrl())
                    .audioUrl(word.getAudioUrl())
                    .isSelectedByAnotherMultipleChoiceExercise(false)
                    .build();

            for (Long wordID : wordIDs) {
                if (builtWord.getWordId() == wordID) {
                    builtWord.setSelectedByAnotherMultipleChoiceExercise(true);
                }
            }

            wordForMultipleChoiceExerciseResponses.add(builtWord);
        }

        return wordForMultipleChoiceExerciseResponses;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<WordForMatchingExerciseResponse> getWordsByMatchingExerciseId(Long exerciseId) {
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseId);

        if (exerciseOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Exercise exercise = exerciseOpt.get();

        if (!exercise.getExerciseType().getId().equals(ExerciseTypeEnum.MATCHING_WORD_EXERCISE.getId())) {
            throw new AppException(ErrorCodeEnum.RESOURCE_CONFLICT);
        }

        List<WordQuery> words = wordRepository.findByExerciseId(exercise.getId());

        List<WordForMatchingExerciseResponse> responses = new ArrayList<>();

        for (WordQuery word : words) {
            WordForMatchingExerciseResponse response = WordForMatchingExerciseResponse.builder()
                    .wordId(word.getId())
                    .englishText(word.getEnglishText())
                    .vietnameseText(word.getVietnameseText())
                    .imageUrl(word.getImageUrl())
                    .audioUrl(word.getAudioUrl())
                    .build();
            responses.add(response);
        }

        return responses;
    }
}
