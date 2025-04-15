package tech.trvihnls.features.ai.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface GroqService {
    String transcribeAudio(MultipartFile audioFile);
     Map<String, String> createEnglishAndVietnameseSong(List<String> words);
}
