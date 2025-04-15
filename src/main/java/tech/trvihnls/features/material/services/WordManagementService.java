// src/main/java/tech/trvihnls/features/material/services/WordManagementService.java
package tech.trvihnls.features.material.services;


import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.features.material.dtos.request.admin.WordRequest;
import tech.trvihnls.features.material.dtos.response.admin.WordImageResponse;
import tech.trvihnls.features.material.dtos.response.admin.WordResponse;

import java.util.List;

public interface WordManagementService {
    List<WordResponse> getAllWords();
    WordResponse getWordById(Long id);
    WordResponse createWord(WordRequest request);
    WordResponse updateWord(Long id, WordRequest request);
    void deleteWordById(Long id);
    WordImageResponse createOrUpdateImage(MultipartFile file, Long wordId);
}
