package training.copilot.homework.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @NotBlank
    @Size(min = 8, max = 30)
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$",
             message = "Password must contain at least one letter and one digit")
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String fullName;
}
