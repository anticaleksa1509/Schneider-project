package rs.raf.AuthService.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.AuthService.annotations.CheckSecurity;
import rs.raf.AuthService.dto.UpdateUserBody;
import rs.raf.AuthService.model.User;
import rs.raf.AuthService.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user){
        try {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully registered!");
            //napraviti da stigne email na tu adresu kao potvrda o registraciji
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CheckSecurity("can_delete")
    @DeleteMapping(value = "/removeUser")
    public ResponseEntity<?> deleteUser(@RequestParam Long id_user,
                                        @RequestHeader("Authorization") String authorization){
        try {
            userService.deleteUser(id_user);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted user" +
                    "with ID " + id_user);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @CheckSecurity("can_read")
    @GetMapping(value = "/allUsers",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization")
                                                  String authorization){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers());
    }

    @CheckSecurity("can_update")
    @PatchMapping(value = "/update",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@RequestParam Long id_user,@RequestBody UpdateUserBody user,
                                        @RequestHeader("Authorization") String authorization){
            try {
                userService.updateUser(id_user,user);
                return ResponseEntity.status(HttpStatus.OK).body("Successfully updated user " +
                        "with ID " + id_user);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
    }
}
