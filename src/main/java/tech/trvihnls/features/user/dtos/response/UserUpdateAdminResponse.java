package tech.trvihnls.features.user.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateAdminResponse {
    private long id;
    private String name;
    private String email;
    @JsonProperty("enabled")
    private boolean enabled;
    @Builder.Default
    List<RoleResponse> roles = new ArrayList<>();
}
