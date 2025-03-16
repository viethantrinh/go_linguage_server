package tech.trvihnls.mobileapis.excercise.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.commons.utils.enums.SpeakerEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"context", "dialogueLines"})
public class DialogueExerciseResponse {
    private String context;

    @Builder.Default
    private List<DialogueLineResponse> dialogueExerciseLines = new ArrayList<>();

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"speaker", "englishText", "vietnameseText", "audioUrl", "displayOrder", "hasBlank", "blankWord"})
    public static class DialogueLineResponse {
        private SpeakerEnum speaker;
        private String englishText;
        private String vietnameseText;
        private String audioUrl;
        private int displayOrder;
        @JsonProperty("hasBlank")
        private boolean hasBlank;
        private String blankWord;
    }
}
