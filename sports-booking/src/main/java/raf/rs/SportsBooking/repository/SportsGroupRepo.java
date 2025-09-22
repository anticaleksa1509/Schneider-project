package raf.rs.SportsBooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raf.rs.SportsBooking.model.SportsGroup;

import java.util.List;
import java.util.Optional;

public interface SportsGroupRepo extends JpaRepository<SportsGroup,Long> {

    Optional<List<SportsGroup>> findByTypeOfSport(String typeOfSport);





}
