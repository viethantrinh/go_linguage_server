package tech.trvihnls.features.excercise.dtos.request.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordArrangementOptionUpdateRequest {
    private Long id;
    private String wordText;
    @JsonProperty("isDistractor")
    private boolean isDistractor;
    private Integer correctPosition;
}