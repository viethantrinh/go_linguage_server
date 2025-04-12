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
import tech.trvihnls.features.material.dtos.response.admin.SentenceForMultipleChoiceExerciseResponse;
import tech.trvihnls.features.material.services.SentenceService;

import java.util.List;

@RestController
@RequestMapping("/sentences")
@RequiredArgsConstructor
public class SentenceController {

    private final SentenceService sentenceService;

    @GetMapping("/by-multiple-choice-exercise/{exerciseId}")
    public ResponseEntity<ApiResponse<List<SentenceForMultipleChoiceExerciseResponse>>> getSentencesByMultipleChoiceExerciseId(
            @PathVariable Long exerciseId) {
        List<SentenceForMultipleChoiceExerciseResponse> sentences = 
                sentenceService.getSentencesByMultipleChoiceExerciseId(exerciseId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, sentences);
    }
}
