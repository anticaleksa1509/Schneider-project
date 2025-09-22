package raf.rs.SportsBooking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Data
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long session_id;

    @ManyToOne
    private Field field;

    @ManyToOne
    private Coach coach;

    @ManyToOne
    private SportsGroup sportsGroup;

    private boolean reserved;

    private LocalTime startTime;
    private LocalTime endTime;
    private DayOfWeek dayOfWeek;
    private String typeOfSession;

}
