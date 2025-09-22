package rs.raf.AuthService.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.AuthService.dto.LoginCredentials;
import rs.raf.AuthService.repositories.UserRepository;
import rs.raf.AuthService.service.LoginService;
import rs.raf.AuthService.service.TokenResponse;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    LoginService loginService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginCredentials loginCredentials){
        try {
            TokenResponse tokenResponse = loginService.login(loginCredentials);
            return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
}
