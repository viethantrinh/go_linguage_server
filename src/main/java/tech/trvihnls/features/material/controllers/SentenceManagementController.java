// src/main/java/tech/trvihnls/features/material/controllers/WordManagementController.java
package tech.trvihnls.features.material.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.material.dtos.request.admin.SentenceRequest;
import tech.trvihnls.features.material.dtos.response.admin.SentenceResponse;
import tech.trvihnls.features.material.services.SentenceManagementService;

import java.util.List;

@RestController
@RequestMapping("/sentences")
@RequiredArgsConstructor
public class SentenceManagementController {

    private final SentenceManagementService sentenceManagementService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SentenceResponse>>> getAllSentences() {
        List<SentenceResponse> sentences = sentenceManagementService.getAllSentences();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, sentences);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SentenceResponse>> getSentenceById(@PathVariable Long id) {
        SentenceResponse sentence = sentenceManagementService.getSentenceById(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, sentence);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SentenceResponse>> createSentence(@RequestBody SentenceRequest request) {
        SentenceResponse createdSentence = sentenceManagementService.createSentence(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, createdSentence);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SentenceResponse>> updateSentence(
            @PathVariable Long id,
            @RequestBody SentenceRequest request) {
        SentenceResponse updatedSentence = sentenceManagementService.updateSentence(id, request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, updatedSentence);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSentence(@PathVariable Long id) {
        sentenceManagementService.deleteSentenceById(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }
}
