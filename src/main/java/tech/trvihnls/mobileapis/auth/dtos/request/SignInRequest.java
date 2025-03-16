package tech.trvihnls.mobileapis.auth.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    private String email;
    private String password;
}
