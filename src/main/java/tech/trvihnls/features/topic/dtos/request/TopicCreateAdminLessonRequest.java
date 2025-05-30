package tech.trvihnls.features.topic.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicCreateAdminLessonRequest {
    private String name;
    private int displayOrder;
    private long lessonTypeId;
}
