package tech.trvihnls.features.lesson.dtos.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonDetailAdminResponse {
    private long id;
    private long lessonTypeId;
    private long topicId;
    private String name;
    @Builder.Default
    private List<LessonDetailExerciseAdminResponse> exercises = new ArrayList<>();
}
