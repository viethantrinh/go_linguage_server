package tech.trvihnls.features.learn.services.impl;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tech.trvihnls.features.ai.services.impl.GroqTranscribeService;
import tech.trvihnls.features.learn.dtos.response.PronounAssessmentResponse;
import tech.trvihnls.features.learn.services.PronounAssessmentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PronounAssessmentServiceImpl implements PronounAssessmentService {

    private final GroqTranscribeService transcribeService;

    private final NormalizedLevenshtein levenshtein = new NormalizedLevenshtein();

    public PronounAssessmentResponse assessPronunciation(MultipartFile audioFile, String referenceText) {
        // Chuyển đổi giọng nói thành văn bản sử dụng Groq Whisper
        String transcribedText = transcribeService.transcribeAudio(audioFile);

        // Chuẩn hóa văn bản để so sánh
        String normalizedReference = normalizeText(referenceText);
        String normalizedTranscribed = normalizeText(transcribedText);

        // Tính độ tương đồng tổng thể
        double overallSimilarity = 1.0 - levenshtein.distance(normalizedReference, normalizedTranscribed);

        // So sánh chi tiết từng từ
        String[] refWords = normalizedReference.split("\\s+");
        String[] transcribedWords = normalizedTranscribed.split("\\s+");
        Map<String, Double> wordScores = compareWordByWord(refWords, transcribedWords);

        // Tạo phản hồi
        List<String> feedback = generateFeedback(overallSimilarity, wordScores, normalizedReference, normalizedTranscribed);

        return new PronounAssessmentResponse(
                overallSimilarity * 100, // Chuyển thành phần trăm
                normalizedTranscribed,
                normalizedReference,
                feedback,
                wordScores
        );
    }

    private String normalizeText(String text) {
        // Chuyển thành chữ thường
        String normalized = text.toLowerCase();

        // Loại bỏ dấu câu
        normalized = normalized.replaceAll("[,.!?;:]", "");

        // Thay thế nhiều khoảng trắng thành một
        normalized = normalized.replaceAll("\\s+", " ");

        // Loại bỏ khoảng trắng ở đầu và cuối
        return normalized.trim();
    }

    private Map<String, Double> compareWordByWord(String[] refWords, String[] transcribedWords) {
        Map<String, Double> wordScores = new HashMap<>();

        for (String refWord : refWords) {
            if (refWord.isEmpty()) continue;

            double bestScore = 0;
            for (String transcribedWord : transcribedWords) {
                if (transcribedWord.isEmpty()) continue;

                double similarity = 1.0 - levenshtein.distance(refWord, transcribedWord);
                if (similarity > bestScore) {
                    bestScore = similarity;
                }
            }
            wordScores.put(refWord, bestScore);
        }

        return wordScores;
    }

    private List<String> generateFeedback(double overallSimilarity,
                                          Map<String, Double> wordScores,
                                          String normalizedReference,
                                          String normalizedTranscribed) {
        List<String> feedback = new ArrayList<>();

        // Phản hồi dựa trên độ tương đồng tổng thể
        if (overallSimilarity >= 0.9) {
            feedback.add("Phát âm rất tốt! Nội dung được nhận dạng chính xác.");
        } else if (overallSimilarity >= 0.7) {
            feedback.add("Phát âm tốt. Có một vài từ không được nhận dạng chính xác.");
        } else if (overallSimilarity >= 0.5) {
            feedback.add("Phát âm cần cải thiện. Nhiều từ không được nhận dạng chính xác.");
        } else {
            feedback.add("Cần luyện tập nhiều hơn. Phát âm chưa đủ rõ ràng để nhận dạng chính xác.");
        }

        // Phân tích và đưa ra gợi ý cho các từ có điểm thấp
        List<String> problematicWords = new ArrayList<>();
        wordScores.forEach((word, score) -> {
            if (score < 0.7 && word.length() > 2) {
                problematicWords.add(word);
            }
        });

        // Thêm phản hồi về từ cụ thể
        if (!problematicWords.isEmpty()) {
            if (problematicWords.size() > 5) {
                feedback.add("Nhiều từ cần cải thiện phát âm, hãy tập trung vào cách phát âm từng từ một.");
            } else {
                feedback.add("Các từ cần cải thiện phát âm: " + String.join(", ", problematicWords));
            }
        }

        // Nhận xét về sự khác biệt giữa câu gốc và câu được nhận dạng
        if (Math.abs(normalizedReference.split("\\s+").length - normalizedTranscribed.split("\\s+").length) > 3) {
            feedback.add("Số lượng từ được nhận dạng khác nhiều so với câu gốc. Hãy cố gắng phát âm đầy đủ tất cả các từ.");
        }

        return feedback;
    }
}
