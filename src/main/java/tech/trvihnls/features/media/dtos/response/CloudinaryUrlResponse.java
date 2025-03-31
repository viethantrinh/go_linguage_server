package tech.trvihnls.features.media.dtos.response;

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
