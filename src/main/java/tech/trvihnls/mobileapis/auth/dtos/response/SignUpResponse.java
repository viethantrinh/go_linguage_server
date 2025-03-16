package tech.trvihnls.mobileapis.auth.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {
    private String token;
}
