package tech.trvihnls.mobileapis.main.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"englishText", "vietnameseText", "imageUrl", "audioUrl", "sentences"})
public class VocabularyResponse {
    private String englishText;
    private String vietnameseText;
    private String imageUrl;
    private String audioUrl;
    private SentenceResponse sentence;

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
}
