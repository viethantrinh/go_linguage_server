package tech.trvihnls.models.dtos.main;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import tech.trvihnls.models.dtos.level.LevelResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"streakPoints", "goPoints", "isSubscribed", "levels"})
public class HomeResponse {
    private int streakPoints;
    private int goPoints;
    @JsonProperty("isSubscribed")
    private boolean isSubscribed;
    @Builder.Default
    private List<LevelResponse> levels = new ArrayList<>();
}
