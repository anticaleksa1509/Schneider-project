package raf.rs.SportsBooking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class SessionDTO {

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String typeOfSession;
    private int numberOfPlayers;


}
