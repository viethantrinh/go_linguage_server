package tech.trvihnls.features.excercise.dtos.response.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordArrangementOptionDetailResponse {
    private long id;
    private long wordArrangementExerciseId;
    private String wordText;
    @JsonProperty("isDistractor")
    private boolean isDistractor;
    private int correctPosition;
}