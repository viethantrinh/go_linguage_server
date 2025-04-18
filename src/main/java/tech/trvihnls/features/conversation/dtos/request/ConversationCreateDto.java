// src/main/java/tech/trvihnls/features/conversation/dtos/request/ConversationCreateDto.java
package tech.trvihnls.features.conversation.dtos.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationCreateDto {
    private String name;
    private int displayOrder;

    @Builder.Default
    private List<ConversationLineCreateDto> lines = new ArrayList<>();
}
