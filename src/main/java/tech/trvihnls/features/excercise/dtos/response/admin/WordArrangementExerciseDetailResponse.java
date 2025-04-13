package tech.trvihnls.features.excercise.dtos.response.admin;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordArrangementExerciseDetailResponse {
    private long exerciseId;
    private String exerciseName;
    private Long sentenceId;
    private SentenceDetail sentence;
    private String sourceLanguage;
    private String targetLanguage;
    @Builder.Default
    private List<WordArrangementOptionDetailResponse> options = new ArrayList<>();
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SentenceDetail {
        private long sentenceId;
        private String englishText;
        private String vietnameseText;
        private String audioUrl;
    }
}