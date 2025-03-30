package tech.trvihnls.mobileapis.lesson.dtos.response;

import lombok.*;
import tech.trvihnls.mobileapis.achievement.dtos.response.AchievementResponse;

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
