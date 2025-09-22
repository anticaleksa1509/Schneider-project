package rs.raf.AuthService.bootStrap;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.AuthService.model.Permissions;
import rs.raf.AuthService.model.Role;
import rs.raf.AuthService.model.User;
import rs.raf.AuthService.repositories.RoleRepository;
import rs.raf.AuthService.repositories.UserRepository;
import rs.raf.AuthService.service.HashPassword;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BootStrapClass implements CommandLineRunner {

    private final UserRepository userRepository;
    private final HashPassword hashPassword;
    private final RoleRepository roleRepository;

    @Getter
    private final List<Role> roles;

    @Override
    public void run(String... args) throws Exception {

        Role adminRole = new Role();
        adminRole.setRoleType("Admin");

        Role editorRole = new Role();
        editorRole.setRoleType("Editor");

        Role viewerRole = new Role();
        viewerRole.setRoleType("Viewer");

        Role coachRole = new Role();
        coachRole.setRoleType("Coach");

        Role userRole = new Role();
        userRole.setRoleType("Client");

        roleRepository.save(adminRole);
        roleRepository.save(userRole);
        roleRepository.save(coachRole);
        roleRepository.save(editorRole);
        roleRepository.save(viewerRole);

        User admin = new User();
        admin.setName("Admin");
        admin.setLastName("AdminAdmin");
        admin.setEmail("admin@gmail.com");
        admin.getPermissions().add(Permissions.CAN_CREATE);
        admin.getPermissions().add(Permissions.CAN_DELETE);
        admin.getPermissions().add(Permissions.CAN_UPDATE);
        admin.getPermissions().add(Permissions.CAN_READ);
        admin.getPermissions().add(Permissions.CAN_SEARCH_SESSIONS);
        admin.getPermissions().add(Permissions.CAN_SCHEDULE_SESSION);
        admin.setPassword(hashPassword.encode("admincar123"));
        admin.setRole(adminRole);
        userRepository.save(admin);


        User editor = new User();
        editor.setName("Elena");
        editor.setLastName("Editor");
        editor.setEmail("editor@gmail.com");
        editor.setPermissions(Set.of(
                Permissions.CAN_CREATE,
                Permissions.CAN_READ,
                Permissions.CAN_UPDATE
        ));
        editor.setPassword(hashPassword.encode("elenacar123"));
        editor.setRole(editorRole);
        userRepository.save(editor);

        // Viewer
        User viewer = new User();
        viewer.setName("Viktor");
        viewer.setLastName("Viewer");
        viewer.setEmail("viewer@gmail.com");
        viewer.setPermissions(Set.of(
                Permissions.CAN_READ
        ));
        viewer.setPassword(hashPassword.encode("viktorcar123"));
        viewer.setRole(viewerRole);
        userRepository.save(viewer);

        roles.add(adminRole);
        roles.add(editorRole);
        roles.add(coachRole);
        roles.add(userRole);



        System.out.println("Inicijalni korisnici su uspesno ubaceni u bazu!");
    }


}
