package tech.trvihnls.features.excercise.dtos.response.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.commons.utils.enums.SpeakerEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "dialogueExerciseId", "speaker", "englishText", "vietnameseText", "audioUrl", " displayOrder", "hasBlank", "blankWord"})
public class DialogueExerciseDetailLineResponse {
    private long id;
    private long dialogueExerciseId;
    private SpeakerEnum speaker;
    private String englishText;
    private String vietnameseText;
    private String audioUrl;
    private int displayOrder;
    @JsonProperty("hasBlank")
    private boolean hasBlank;
    private String blankWord;
}
