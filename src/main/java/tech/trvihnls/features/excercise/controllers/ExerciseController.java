package tech.trvihnls.features.excercise.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.excercise.dtos.request.admin.VocabularyExerciseCreateRequest;
import tech.trvihnls.features.excercise.dtos.request.admin.VocabularyExerciseUpdateRequest;
import tech.trvihnls.features.excercise.dtos.response.admin.VocabularyExerciseDetailResponse;
import tech.trvihnls.features.excercise.services.ExerciseService;

@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseService exerciseService;

    @PostMapping("/vocabulary")
    public ResponseEntity<ApiResponse<Void>> createVocabularyExercise(
            @RequestBody VocabularyExerciseCreateRequest request) {
        exerciseService.createVocabularyExercise(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }

    @GetMapping("/vocabulary/{exerciseId}")
    public ResponseEntity<ApiResponse<VocabularyExerciseDetailResponse>> getVocabularyExerciseDetail(
            @PathVariable Long exerciseId) {
        VocabularyExerciseDetailResponse response = exerciseService.getVocabularyExerciseDetail(exerciseId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PutMapping("/vocabulary")
    public ResponseEntity<ApiResponse<Void>> updateVocabularyExercise(
            @RequestBody VocabularyExerciseUpdateRequest request) {
        exerciseService.updateVocabularyExercise(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }
}
