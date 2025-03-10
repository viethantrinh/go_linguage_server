package tech.trvihnls.models.dtos.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloudinaryUrlResponse {
    private String url;
    private String secureUrl;
}
