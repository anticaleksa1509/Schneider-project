package rs.raf.AuthService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import rs.raf.AuthService.bootStrap.BootStrapClass;
import rs.raf.AuthService.dto.UpdateUserBody;
import rs.raf.AuthService.model.Permissions;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.model.User;
import rs.raf.AuthService.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HashPassword hashPassword;
    private final PasswordCheck passwordCheck;
    private final BootStrapClass bootStrapClass;
    private final JmsTemplate jmsTemplate;

    @Value("${destination.registerUser}")
    private String registerDestination;

    public void deleteUser(Long id_user){
        Optional<User> optionalUser = userRepository.findById(id_user);
        if(optionalUser.isEmpty())
            throw new IllegalArgumentException("User with that ID does not exist");

        User user = optionalUser.get();
        userRepository.delete(user);
    }

    public Role getUserRole(){
        List<Role> roles = bootStrapClass.getRoles();
        Role targetRole = null;
        for(Role r : roles) {
            if (r.getRoleType().equalsIgnoreCase("Client")) {
                targetRole = r;
                break;
            }
        }
        return targetRole;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void updateUser(Long id_user, UpdateUserBody user){
        Optional<User> optionalUser = userRepository.findById(id_user);
        if(optionalUser.isEmpty())
            throw new IllegalArgumentException("User with that ID does not exist!");

        User targetUser = optionalUser.get();

        //ukoliko pozovem isBlank na null polje baca mi NullPointerException!!
        if(!(user.getName() == null) && !user.getName().isBlank())
            targetUser.setName(user.getName());

        if(!(user.getLastName() == null) &&!user.getLastName().isBlank())
            targetUser.setLastName(user.getLastName());

        if(!(user.getEmail() == null) && !user.getEmail().isBlank()){
            if(userRepository.existsByEmail(user.getEmail()))
                throw new IllegalArgumentException("Email address already exist.\n" +
                        "Email address must be unique!");
            targetUser.setEmail(user.getEmail());
        }

        if(!(user.getPassword() == null) &&!user.getPassword().isBlank()){
            if(!passwordCheck.checkPasswordNumber(user.getPassword()))
                throw new IllegalArgumentException("Password must contain at least one number!");
            if(!passwordCheck.checkPasswordLength(user.getPassword()))
                throw new IllegalArgumentException("Password length must be between 6 and 16 characters!");

            targetUser.setPassword(hashPassword.encode(user.getPassword()));
        }

        userRepository.save(targetUser);

    }

    public void registerUser(User user){

        if(userRepository.existsByEmail(user.getEmail()))
            throw new IllegalArgumentException("Email address already exists.\n" +
                    "Email address must be unique!");

        if(!passwordCheck.checkPasswordLength(user.getPassword()))
            throw new IllegalArgumentException("Password must contains between 6 and 16 characters");

        if(!passwordCheck.checkPasswordNumber(user.getPassword()))
            throw new IllegalArgumentException("Password must contain at least one number.");


        user.setPermissions(Set.of(Permissions.CAN_UPDATE,Permissions.CAN_READ));
        //korisnik moze sebe da updatuje samo svoje podatke i moze da ima pregled o ostalim korisnicima

        user.setPassword(hashPassword.encode(user.getPassword()));
        user.setRole(getUserRole());

        userRepository.save(user);

        jmsTemplate.convertAndSend(registerDestination,
                user.getId_user() + " " + user.getName() + " " + user.getLastName());

    }
}
