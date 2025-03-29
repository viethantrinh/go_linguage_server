package tech.trvihnls.mobileapis.main.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"isChangeSpeaker", "englishText", "vietnameseText", "audioUrl", "displayOrder", "blankWord"})
public class DialogueLineResponse {
    @JsonProperty("isChangeSpeaker")
    private boolean isChangeSpeaker;
    private String englishText;
    private String vietnameseText;
    private String audioUrl;
    private int displayOrder;
    private String blankWord;
}
