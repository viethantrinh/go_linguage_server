package tech.trvihnls.features.user.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateAdminRequest {
    private long id;
    private String name;
    @JsonProperty("isEnabled")
    private boolean isEnabled;
    private String role;
}
