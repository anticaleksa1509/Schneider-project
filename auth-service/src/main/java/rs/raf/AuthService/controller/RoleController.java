package rs.raf.AuthService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.AuthService.annotations.CheckRolePermission;
import rs.raf.AuthService.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @CheckRolePermission(value = {"Admin","Editor"})
    @PostMapping(value = "/addRoleToUser")
    public ResponseEntity<?> addRoleToUser(@RequestParam Long id_user, @RequestParam String roleType,
                                           @RequestHeader("Authorization") String authorization){
        try {
            roleService.addRoleToUser(id_user,roleType);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Successfully added role: " + roleType + " to user with ID " + id_user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
