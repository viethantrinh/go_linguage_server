package tech.trvihnls.mobileapis.topic.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "imageUrl", "totalUserXPPoints", "isPremium", "displayOrder"})
public class TopicResponse {
    private long id;
    private String name;
    private String imageUrl;
    private int displayOrder;
    private boolean isPremium;
    private int totalUserXPPoints;
}
