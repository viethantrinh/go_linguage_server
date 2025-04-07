package tech.trvihnls.features.topic.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicImageResponse {
    private long id;

    private String name;

    private String imageUrl;

    @JsonProperty("isPremium")
    private boolean isPremium;

    private int displayOrder;
}
