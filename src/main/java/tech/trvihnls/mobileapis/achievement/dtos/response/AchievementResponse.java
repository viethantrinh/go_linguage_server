package tech.trvihnls.mobileapis.achievement.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"name", "description", "imageUrl"})
public class AchievementResponse {
    private String name;
    private String description;
    private String imageUrl;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int current;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int target;
}
