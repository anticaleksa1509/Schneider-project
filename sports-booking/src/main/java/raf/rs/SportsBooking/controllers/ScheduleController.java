package raf.rs.SportsBooking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.rs.SportsBooking.annotations.CheckSecurity;
import raf.rs.SportsBooking.dto.SessionDTO;
import raf.rs.SportsBooking.model.Session;
import raf.rs.SportsBooking.service.SessionService;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {

    @Autowired
    private SessionService sessionService;

    @CheckSecurity("can_schedule_session")
    @PostMapping(value = "/session")
    public ResponseEntity<?> scheduleSession(@RequestParam LocalDateTime time, @RequestBody SessionDTO session
            , @RequestHeader("Authorization") String authorization){

        try {
            sessionService.scheduleSession(time,session);
            return new ResponseEntity<>("Successfully scheduled session!",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong",HttpStatus.BAD_REQUEST);
        }
    }

}
