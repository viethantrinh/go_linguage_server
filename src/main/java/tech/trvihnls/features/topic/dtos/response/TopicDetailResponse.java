package tech.trvihnls.features.topic.dtos.response;

import lombok.*;
import tech.trvihnls.features.lesson.dtos.response.LessonDetailResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicDetailResponse {
    @Builder.Default
    private List<LessonDetailResponse> lessonDetails = new ArrayList<>();
}
