package tech.trvihnls.features.lesson.dtos.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonUpdateAdminRequest {
    private long id;
    private String name;
    private long lessonTypeId;
    private long topicId;
    @Builder.Default
    private List<LessonUpdateExerciseAdminRequest> exercises = new ArrayList<>();
}
