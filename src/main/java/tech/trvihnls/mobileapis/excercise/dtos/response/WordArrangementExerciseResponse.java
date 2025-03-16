package tech.trvihnls.mobileapis.excercise.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.commons.utils.enums.LanguageEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"sourceLanguage", "targetLanguage", "sentence", "words"})
public class WordArrangementExerciseResponse {
    private LanguageEnum sourceLanguage;
    private LanguageEnum targetLanguage;
    private SentenceResponse sentence;
    @Builder.Default
    private List<WordResponse> words = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"englishText", "vietnameseText", "audioUrl"})
    public static class SentenceResponse {
        private String englishText;
        private String vietnameseText;
        private String audioUrl;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"text", "isDistractor", "correctPosition"})
    public static class WordResponse {
        private String text;
        @JsonProperty("isDistractor")
        private boolean isDistractor;
        private int correctPosition;
    }
}
