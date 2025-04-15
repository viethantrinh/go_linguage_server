// src/main/java/tech/trvihnls/features/material/controllers/WordManagementController.java
package tech.trvihnls.features.material.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.commons.dtos.ApiResponse;
import tech.trvihnls.commons.utils.ResponseUtils;
import tech.trvihnls.commons.utils.enums.SuccessCodeEnum;
import tech.trvihnls.features.material.dtos.request.admin.WordRequest;
import tech.trvihnls.features.material.dtos.response.admin.WordImageResponse;
import tech.trvihnls.features.material.dtos.response.admin.WordResponse;
import tech.trvihnls.features.material.services.WordManagementService;

import java.util.List;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordManagementController {

    private final WordManagementService wordManagementService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WordResponse>>> getAllWords() {
        List<WordResponse> words = wordManagementService.getAllWords();
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, words);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WordResponse>> getWordById(@PathVariable Long id) {
        WordResponse word = wordManagementService.getWordById(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, word);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WordResponse>> createWord(@RequestBody WordRequest request) {
        WordResponse createdWord = wordManagementService.createWord(request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, createdWord);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WordResponse>> updateWord(
            @PathVariable Long id, 
            @RequestBody WordRequest request) {
        WordResponse updatedWord = wordManagementService.updateWord(id, request);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, updatedWord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWord(@PathVariable Long id) {
        wordManagementService.deleteWordById(id);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, null);
    }

    @PostMapping("/upload-image")
    public ResponseEntity<ApiResponse<WordImageResponse>> uploadImageForWord(
            @RequestParam("file") MultipartFile file,
            @RequestParam("wordId") Long wordId
    ) {
        WordImageResponse response = wordManagementService.createOrUpdateImage(file, wordId);
        return ResponseUtils.success(SuccessCodeEnum.GENERAL_SUCCESS, response);
    }
}
