package tech.trvihnls.features.topic.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicUpdateAdminRequest {
    private long id;

    private long levelTypeId;

    private String name;

    @JsonProperty("isPremium")
    private boolean isPremium;

    @Builder.Default
    private List<TopicUpdateAdminLessonRequest> lessons = new ArrayList<>();
}
