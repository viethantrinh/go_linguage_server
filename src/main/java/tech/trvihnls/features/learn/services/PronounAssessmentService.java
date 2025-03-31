package tech.trvihnls.features.learn.services;

import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.features.learn.dtos.response.PronounAssessmentResponse;

public interface PronounAssessmentService {
    PronounAssessmentResponse assessPronunciation(MultipartFile audioFile, String referenceText);
}
