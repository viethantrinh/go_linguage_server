package tech.trvihnls.mobileapis.conversation.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"englishText", "vietnameseText", "audioUrl", "gender"})
public class ConversationUserOptionResponse {
    private String englishText;
    private String vietnameseText;
    private String audioUrl;
    private String gender;
}
