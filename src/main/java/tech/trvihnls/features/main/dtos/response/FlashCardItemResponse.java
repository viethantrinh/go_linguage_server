package tech.trvihnls.features.main.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"englishText", "vietnameseText", "imageUrl", "audioUrl"})
public class FlashCardItemResponse {
    private String englishText;
    private String vietnameseText;
    private String imageUrl;
    private String audioUrl;
}
