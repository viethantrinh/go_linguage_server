package tech.trvihnls.features.excercise.dtos.request.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import tech.trvihnls.commons.utils.enums.SpeakerEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialogueExerciseLineCreateRequest {
    private SpeakerEnum speaker;
    private String englishText;
    private String vietnameseText;
    private String displayOrder;
    @JsonProperty("hasBlank")
    private boolean hasBlank;
    private String blankWord;
}
