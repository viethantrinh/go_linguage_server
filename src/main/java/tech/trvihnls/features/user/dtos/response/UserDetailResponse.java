package tech.trvihnls.features.user.dtos.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    private long id;
    private String name;
    private String email;
    private boolean enabled;
    @Builder.Default
    List<RoleResponse> roles = new ArrayList<>();
}
