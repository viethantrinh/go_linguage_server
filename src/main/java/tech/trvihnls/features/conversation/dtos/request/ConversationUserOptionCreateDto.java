// src/main/java/tech/trvihnls/features/conversation/dtos/request/ConversationUserOptionCreateDto.java
package tech.trvihnls.features.conversation.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationUserOptionCreateDto {
    private String englishText;
    private String vietnameseText;
}
