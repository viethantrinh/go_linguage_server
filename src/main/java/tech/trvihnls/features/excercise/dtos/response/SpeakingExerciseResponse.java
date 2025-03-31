package tech.trvihnls.features.excercise.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"englishText", "vietnameseText", "audioUrl"})
public class SpeakingExerciseResponse {
    private String englishText;
    private String vietnameseText;
    private String audioUrl;
}
