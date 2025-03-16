package tech.trvihnls.mobileapis.lesson.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.mobileapis.excercise.dtos.response.ExerciseDetailResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "name", "totalUserXPPoints", "lessonType", "displayOrder", "exercises"})
public class LessonDetailResponse {
    private long id;
    private String name;
    private int totalUserXPPoints;
    private int lessonType; // lesson type id
    private int displayOrder;
    @Builder.Default
    private List<ExerciseDetailResponse<?>> exercises = new ArrayList<>();
}
