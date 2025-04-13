package tech.trvihnls.features.excercise.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.excercise.dtos.request.admin.*;
import tech.trvihnls.features.excercise.dtos.response.admin.MatchingExerciseDetailResponse;
import tech.trvihnls.features.excercise.dtos.response.admin.MultipleChoiceExerciseDetailResponse;
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

    @GetMapping("/multiple-choice/{exerciseId}")
    public ResponseEntity<ApiResponse<MultipleChoiceExerciseDetailResponse>> getMultipleChoiceExerciseDetail(
            @PathVariable Long exerciseId) {
        MultipleChoiceExerciseDetailResponse response = exerciseService.getMultipleChoiceExerciseDetailResponse(exerciseId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping("/multiple-choice")
    public ResponseEntity<ApiResponse<Void>> createMultipleChoiceExercise(
            @RequestBody MultipleChoiceExerciseCreateRequest request) {
        exerciseService.createMultipleChoiceExercise(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }

    @PutMapping("/multiple-choice")
    public ResponseEntity<ApiResponse<Void>> updateMultipleChoiceExercise(
            @RequestBody MultipleChoiceExerciseUpdateRequest request) {
        exerciseService.updateMultipleChoiceExercise(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }

    @GetMapping("/matching/{exerciseId}")
    public ResponseEntity<ApiResponse<MatchingExerciseDetailResponse>> getMatchingExerciseDetail(
            @PathVariable Long exerciseId) {
        MatchingExerciseDetailResponse response = exerciseService.getMatchingExerciseDetail(exerciseId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }

    @PostMapping("/matching")
    public ResponseEntity<ApiResponse<Void>> createMatchingExercise(
            @RequestBody MatchingExerciseCreateRequest request) {
        exerciseService.createMatchingExercise(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }

    @PutMapping("/matching")
    public ResponseEntity<ApiResponse<Void>> updateMatchingExercise(
            @RequestBody MatchingExerciseUpdateRequest request) {
        exerciseService.updateMatchingExercise(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }
}
