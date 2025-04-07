package tech.trvihnls.features.topic.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDetailAdminLessonResponse {
    private long id;
    private long lessonTypeId;
    private String name;
    private int displayOrder;
}
