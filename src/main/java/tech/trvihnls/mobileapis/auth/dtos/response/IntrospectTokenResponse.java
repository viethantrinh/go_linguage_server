package tech.trvihnls.mobileapis.auth.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntrospectTokenResponse {
    private boolean valid;
}
