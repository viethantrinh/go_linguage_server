package tech.trvihnls.features.lesson.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonCreateExerciseAdminRequest {
    private String name;
    private long exerciseTypeId;
    private int displayOrder;
}
