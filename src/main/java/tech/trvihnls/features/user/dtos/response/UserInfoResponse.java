package tech.trvihnls.features.user.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import tech.trvihnls.features.achievement.dtos.response.AchievementResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {
    private Long id;
    private String name;
    private String email;
    private int totalXPPoints;
    private int totalGoPoints;
    @Builder.Default
    private List<AchievementResponse> achievements = new ArrayList<>();
}
