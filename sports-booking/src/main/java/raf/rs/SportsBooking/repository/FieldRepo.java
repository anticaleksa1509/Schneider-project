package raf.rs.SportsBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.rs.SportsBooking.model.Field;

import java.util.Optional;

public interface FieldRepo extends JpaRepository<Field,Long> {

    Optional<Field> findByMark(String mark);
    boolean existsByMark(String mark);
}
