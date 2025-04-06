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

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int totalXPPoints;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int totalGoPoints;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Builder.Default
    private List<AchievementResponse> achievements = new ArrayList<>();
}
