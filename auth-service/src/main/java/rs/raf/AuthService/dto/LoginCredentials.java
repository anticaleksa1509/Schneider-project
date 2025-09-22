package rs.raf.AuthService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginCredentials {

    @NotBlank(message = "Email is mandatory!")
    private String email;
    @NotBlank(message = "Password is mandatory!")
    private String password;
}
