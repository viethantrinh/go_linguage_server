package tech.trvihnls.features.topic.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicLessonRequest {
    private String name;
    private int displayOrder;
    private long lessonTypeId;
}
