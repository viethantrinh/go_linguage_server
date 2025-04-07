package tech.trvihnls.features.topic.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicUpdateAdminLessonRequest {
    private long id;
    private long lessonTypeId;
    private String name;
    private int displayOrder;
}
