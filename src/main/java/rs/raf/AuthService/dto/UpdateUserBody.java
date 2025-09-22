package rs.raf.AuthService.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserBody {
    //stvari koje obican korisnik moze da menja
    //zato koristimo ovakvu klasu bez permisija u njoj
    private String name;
    private String lastName;

    @Email
    private String email;

    private String password;
}
