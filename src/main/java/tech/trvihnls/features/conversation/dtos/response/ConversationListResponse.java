// src/main/java/tech/trvihnls/features/conversation/dtos/response/ConversationListResponse.java
package tech.trvihnls.features.conversation.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "displayOrder", "imageUrl", "createdAt", "lineCount"})
public class ConversationListResponse {
    private long id;
    private String name;
    private int displayOrder;
    private String imageUrl;
    private String createdAt;
    private int lineCount;
}
