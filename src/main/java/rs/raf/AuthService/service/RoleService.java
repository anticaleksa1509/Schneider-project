package rs.raf.AuthService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rs.raf.AuthService.dto.SendUser;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.model.User;
import rs.raf.AuthService.repositories.RoleRepository;
import rs.raf.AuthService.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {


    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    //restTemplate - sinhrona komunikacija
    public void addRoleToUser(Long id_user, String roleType){

        Optional<Role> optionalRole = roleRepository.findRoleByRoleType(roleType);
        Optional<User> optionalUser = userRepository.findById(id_user);

        if(optionalRole.isEmpty())
            throw new IllegalArgumentException("Role type does not exist!");
        if(optionalUser.isEmpty())
            throw new IllegalArgumentException("User with that ID does not exist!");

        User user = optionalUser.get();
        Role role = optionalRole.get();

        user.setRole(role);

        userRepository.save(user);

    }



}
