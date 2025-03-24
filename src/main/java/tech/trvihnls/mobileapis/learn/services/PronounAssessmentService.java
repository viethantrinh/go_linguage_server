package tech.trvihnls.mobileapis.learn.services;

import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.mobileapis.learn.dtos.response.PronounAssessmentResponse;

public interface PronounAssessmentService {
    PronounAssessmentResponse assessPronunciation(MultipartFile audioFile, String referenceText);
}
