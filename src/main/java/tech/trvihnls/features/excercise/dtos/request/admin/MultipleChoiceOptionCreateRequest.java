package tech.trvihnls.features.excercise.dtos.request.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceOptionCreateRequest {
    private Long wordId;
    private Long sentenceId;
    private String optionType;
    @JsonProperty("isCorrect")
    private boolean isCorrect;
}
