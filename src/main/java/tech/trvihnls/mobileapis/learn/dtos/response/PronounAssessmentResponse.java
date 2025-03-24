package tech.trvihnls.mobileapis.learn.dtos.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PronounAssessmentResponse {
    private double score;
    private String transcribedText;
    private String referenceText;
    private List<String> feedback;
    private Map<String, Double> wordScores;
}
