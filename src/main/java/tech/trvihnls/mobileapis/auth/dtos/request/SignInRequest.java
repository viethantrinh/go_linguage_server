package tech.trvihnls.mobileapis.auth.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @NotNull(message = "Email must not be null")
    @Length(min = 4, max = 64, message = "Email must be between 4 and 64 characters")
    @Email(message = "Email must be valid")
    private String email;

    @NotNull(message = "Email must not be null")
    @Length(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;
}
