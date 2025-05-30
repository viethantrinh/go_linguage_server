// src/main/java/tech/trvihnls/features/material/services/WordManagementService.java
package tech.trvihnls.features.material.services;


import tech.trvihnls.features.material.dtos.request.admin.SentenceRequest;
import tech.trvihnls.features.material.dtos.response.admin.SentenceResponse;

import java.util.List;

public interface SentenceManagementService {
    List<SentenceResponse> getAllSentences();
    SentenceResponse getSentenceById(Long id);
    SentenceResponse createSentence(SentenceRequest request);
    SentenceResponse updateSentence(Long id, SentenceRequest request);
    void deleteSentenceById(Long id);
}
