package tech.trvihnls.mobileapis.conversation.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.commons.utils.enums.ConversationEntryTypeEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "type", "systemEnglishText", "systemVietnameseText", "systemAudioUrl", "displayOrder", "options"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationLineResponse {

    private long id;
    private ConversationEntryTypeEnum type;
    private String systemEnglishText;
    private String systemVietnameseText;
    private String systemAudioUrl;
    private int displayOrder;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Builder.Default
    private List<ConversationUserOptionResponse> options = new ArrayList<>();
}
