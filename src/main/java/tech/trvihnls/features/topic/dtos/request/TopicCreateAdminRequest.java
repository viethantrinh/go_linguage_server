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
public class TopicCreateAdminRequest {
    private String name;

    @Builder.Default
    private List<TopicLessonRequest> lessons = new ArrayList<>();

    @JsonProperty("isPremium")
    private boolean isPremium;

    private long levelTypeId;
}
