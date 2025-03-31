package tech.trvihnls.features.auth.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntrospectTokenRequest {
    private String token;
}
