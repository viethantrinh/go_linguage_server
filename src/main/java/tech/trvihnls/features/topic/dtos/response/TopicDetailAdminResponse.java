package tech.trvihnls.features.topic.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "levelId", "name", "imageUrl", "displayOrder", "isPremium", "lessons"})
public class TopicDetailAdminResponse {
    private long id;

    private long levelId;

    private String name;

    private String imageUrl;

    private int displayOrder;

    @JsonProperty("isPremium")
    private boolean isPremium;


    @Builder.Default
    private List<TopicDetailAdminLessonResponse> lessons = new ArrayList<>();
}
