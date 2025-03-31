package tech.trvihnls.features.lesson.dtos.response;

import lombok.*;
import tech.trvihnls.features.achievement.dtos.response.AchievementResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonSubmitResponse {
    @Builder.Default
    private List<AchievementResponse> achievements = new ArrayList<>();
}
