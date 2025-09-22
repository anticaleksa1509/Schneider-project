package rs.raf.AuthService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.AuthService.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findUserByEmailAndPassword(String email,String Password);

    boolean existsByEmail(String email);
}
