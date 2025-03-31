package tech.trvihnls.features.conversation.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "isChangeSpeaker", "systemEnglishText", "systemVietnameseText", "systemAudioUrl", "displayOrder", "options"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationLineResponse {

    private long id;
    @JsonProperty("isChangeSpeaker")
    private boolean isChangeSpeaker;
    private String systemEnglishText;
    private String systemVietnameseText;
    private String systemAudioUrl;
    private int displayOrder;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Builder.Default
    private List<ConversationUserOptionResponse> options = new ArrayList<>();
}
