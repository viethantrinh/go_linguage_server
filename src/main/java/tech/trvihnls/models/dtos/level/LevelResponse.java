package tech.trvihnls.models.dtos.level;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.models.dtos.topic.TopicResponse;

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
