package tech.trvihnls.mobileapis.topic.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.mobileapis.lesson.dtos.response.LessonDetailResponse;

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
