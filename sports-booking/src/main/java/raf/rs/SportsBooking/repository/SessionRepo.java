package raf.rs.SportsBooking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import raf.rs.SportsBooking.model.Coach;
import raf.rs.SportsBooking.model.Field;
import raf.rs.SportsBooking.model.Session;
import raf.rs.SportsBooking.model.SportsGroup;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepo extends JpaRepository<Session,Long> {

    Optional<Session> findSessionByFieldAndDayOfWeekAndStartTime(Field field, DayOfWeek dayOfWeek, LocalTime startTime);
    Optional<Session> findSessionBySportsGroup(SportsGroup sportsGroup);
    Optional<Session> findSessionByCoach(Coach coach);
    Optional<Session> findSessionByField(Field field);

    Optional<List<Session>> findAllByTypeOfSession(String type);
    Optional<List<Session>> findAllByDayOfWeek(DayOfWeek dayOfWeek, Sort sort);

    Optional<List<Session>> findAllByDayOfWeekAndField(DayOfWeek dayOfWeek,Field field);

    Optional<List<Session>> findAllBySportsGroup_Type(String type);

    Optional<List<Session>> findAllByTypeOfSessionAndDayOfWeek(String typeOfSession,
                                                               DayOfWeek dayOfWeek);

}
