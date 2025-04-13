package tech.trvihnls.features.material.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.material.dtos.response.admin.WordForMatchingExerciseResponse;
import tech.trvihnls.features.material.dtos.response.admin.WordForMultipleChoiceExerciseResponse;
import tech.trvihnls.features.material.dtos.response.admin.WordForVocabularyExerciseResponse;
import tech.trvihnls.features.material.services.WordService;

import java.util.List;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping("/by-vocabulary-exercise/{exerciseId}")
    public ResponseEntity<ApiResponse<List<WordForVocabularyExerciseResponse>>> getWordsByVocabularyExerciseId(@PathVariable Long exerciseId) {
        List<WordForVocabularyExerciseResponse> words = wordService.getWordsByVocabularyExerciseId(exerciseId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, words);
    }

    @GetMapping("/by-multiple-choice-exercise/{exerciseId}")
    public ResponseEntity<ApiResponse<List<WordForMultipleChoiceExerciseResponse>>> getWordsByMultipleChoiceExerciseId(@PathVariable Long exerciseId) {
        List<WordForMultipleChoiceExerciseResponse> words = wordService.getWordsByMultipleChoiceExerciseId(exerciseId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, words);
    }

    @GetMapping("/by-matching-exercise/{exerciseId}")
    public ResponseEntity<ApiResponse<List<WordForMatchingExerciseResponse>>> getWordsByMatchingExerciseId(
            @PathVariable Long exerciseId) {
        List<WordForMatchingExerciseResponse> words = wordService.getWordsByMatchingExerciseId(exerciseId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, words);
    }
}
