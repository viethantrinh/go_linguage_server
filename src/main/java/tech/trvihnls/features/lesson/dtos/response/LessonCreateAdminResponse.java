package tech.trvihnls.features.lesson.dtos.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreateAdminResponse {
    private long id;
    private long lessonTypeId;
    private String name;
    private String topicName;
    private int displayOrder;
    private LocalDateTime createdAt;
}
