package tech.trvihnls.features.material.dtos.response.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SentenceForWordArrangementExerciseResponse {
    private long sentenceId;
    private String englishText;
    private String vietnameseText;
    private String audioUrl;

    @JsonProperty("isSelectedByAnotherExercise")
    private boolean isSelectedByAnotherExercise;
}
