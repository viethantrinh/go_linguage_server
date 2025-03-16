package tech.trvihnls.mobileapis.level.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.mobileapis.topic.dtos.response.TopicResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "imageUrl", "totalUserXPPoints", "displayOrder", "topics"})
public class LevelResponse {
    private long id;
    private String name;
    private int totalUserXPPoints;
    private int displayOrder;

    @Builder.Default
    private List<TopicResponse> topics = new ArrayList<>();
}
