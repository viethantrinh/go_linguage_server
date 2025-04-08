package tech.trvihnls.features.lesson.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonUpdateExerciseAdminRequest {
    private Long id;
    private String name;
    private long exerciseTypeId;
    private int displayOrder;
}
