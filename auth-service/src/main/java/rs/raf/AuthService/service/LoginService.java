package rs.raf.AuthService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import rs.raf.AuthService.dto.LoginCredentials;
import rs.raf.AuthService.model.Permissions;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.model.User;
import rs.raf.AuthService.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final HashPassword hashPassword;
    private final JmsTemplate jmsTemplate;

    @Value("${destination.loginUser}")
    private String destinationTopic;
    public TokenResponse login(LoginCredentials loginCredentials) {

        Optional<User> optionalUser = userRepository.findUserByEmailAndPassword(
                loginCredentials.getEmail(),hashPassword.encode(loginCredentials.getPassword()));

        if (optionalUser.isEmpty())
            throw new IllegalArgumentException("Email or password are incorrect");

        User user = optionalUser.get();

        Claims claims = Jwts.claims();
        claims.put("id_user", user.getId_user());
        claims.put("can_create", user.getPermissions().contains(Permissions.CAN_CREATE));
        claims.put("can_delete", user.getPermissions().contains(Permissions.CAN_DELETE));
        claims.put("can_read", user.getPermissions().contains(Permissions.CAN_READ));
        claims.put("can_update", user.getPermissions().contains(Permissions.CAN_UPDATE));
        claims.put("can_search_sessions",user.getPermissions().contains(Permissions.CAN_SEARCH_SESSIONS));
        claims.put("can_schedule_session",user.getPermissions().contains(Permissions.CAN_SCHEDULE_SESSION));
        claims.put("role",user.getRole().getRoleType());

       jmsTemplate.convertAndSend(destinationTopic,
               user.getId_user() + " " + user.getName() + " " + user.getLastName());


        return new TokenResponse(tokenService.generateToken(claims));

    }
}
