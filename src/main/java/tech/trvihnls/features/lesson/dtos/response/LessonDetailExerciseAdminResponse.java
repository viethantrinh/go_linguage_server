package tech.trvihnls.features.lesson.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDetailExerciseAdminResponse {
    private long id;
    private String name;
    private long exerciseTypeId;
    private int displayOrder;
}
