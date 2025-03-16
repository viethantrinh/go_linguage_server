package tech.trvihnls.mobileapis.excercise.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "englishText", "vietnameseText", "imageUrl", "audioUrl"})
public class MatchingExerciseResponse {
    private long id;
    private String englishText;
    private String vietnameseText;
    private String imageUrl;
    private String audioUrl;
}
