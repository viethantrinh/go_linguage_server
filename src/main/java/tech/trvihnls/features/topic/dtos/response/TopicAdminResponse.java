package tech.trvihnls.features.topic.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "imageUrl", "isPremium", "displayOrder", "createdDate"})
public class TopicAdminResponse {
    private long id;
    private String name;
    private String imageUrl;
    private int displayOrder;
    private boolean isPremium;
    private LocalDateTime createdAt;
}
