package tech.trvihnls.models.dtos.media;

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
