package tech.trvihnls.mobileapis.auth.dtos.request;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignOutRequest {
    private String token;
}
