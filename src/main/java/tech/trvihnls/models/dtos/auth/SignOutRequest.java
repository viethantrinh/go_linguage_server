package tech.trvihnls.models.dtos.auth;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignOutRequest {
    private String token;
}
