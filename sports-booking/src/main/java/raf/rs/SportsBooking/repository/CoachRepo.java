package raf.rs.SportsBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.rs.SportsBooking.model.Coach;

import java.util.List;
import java.util.Optional;

public interface CoachRepo extends JpaRepository<Coach,Long> {

    Optional<List<Coach>> findCoachByNameAndLastName(String name, String lastName);
    boolean existsByEmail(String email);
}
