package tech.trvihnls.features.conversation.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "imageUrl", "displayOrder", "isPremium", "lines"})

public class ConversationResponse {
    private long id;
    private String name;
    private String imageUrl;
    private int displayOrder;
    private boolean isPremium;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Builder.Default
    private List<ConversationLineResponse> lines = new ArrayList<>();
}
