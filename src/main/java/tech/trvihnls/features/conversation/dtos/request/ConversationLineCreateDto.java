// src/main/java/tech/trvihnls/features/conversation/dtos/request/ConversationLineCreateDto.java
package tech.trvihnls.features.conversation.dtos.request;

import lombok.*;
import tech.trvihnls.commons.utils.enums.ConversationEntryTypeEnum;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationLineCreateDto {
    private ConversationEntryTypeEnum type;
    private String englishText;
    private String vietnameseText;
    
    @Builder.Default
    private List<ConversationUserOptionCreateDto> options = new ArrayList<>();
}
