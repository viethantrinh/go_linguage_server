package tech.trvihnls.mobileapis.user.dtos.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    private Long id;
    private String name;
}
