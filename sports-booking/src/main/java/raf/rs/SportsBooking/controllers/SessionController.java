package raf.rs.SportsBooking.controllers;

import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.rs.SportsBooking.annotations.CheckSecurity;
import raf.rs.SportsBooking.model.Session;
import raf.rs.SportsBooking.model.SportsGroup;
import raf.rs.SportsBooking.service.SessionService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    SessionService sessionService;


    @CheckSecurity("can_search_sessions")
    @GetMapping(value = "/allSessions",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllSessions(@RequestHeader("Authorization") String authorization){
        try {
            List<Session> allSessions = sessionService.getAllSessions();
            return ResponseEntity.status(HttpStatus.FOUND).body(allSessions);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(value = "/createSession",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createSession(@RequestParam Long coach_id,
                                                @RequestParam Long group_id, @RequestParam Long field_id,
                                                @RequestBody Session session){
        try {
            String message = sessionService.createSession(coach_id,group_id,field_id,session);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }


    @DeleteMapping(value = "/deleteByParams")
    public ResponseEntity<?> getSessionByParameters(@RequestParam DayOfWeek dayOfWeek,
                                                    @RequestParam LocalTime startTime,@RequestParam Long field_id){
        try {
            Session session = sessionService.getSessionByParameters(dayOfWeek,startTime,field_id);
            sessionService.deleteSession(session);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Successfully deleted");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "/getByCoach",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByCoach(@RequestParam Long coach_id){
        try {
            Session session = sessionService.getSessionByCoach(coach_id);
            return ResponseEntity.status(HttpStatus.FOUND).body(session);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping(value = "/getByField",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByField(@RequestParam Long field_id){
        try {
            Session session = sessionService.getSessionByField(field_id);
            return ResponseEntity.status(HttpStatus.FOUND).body(session);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping(value = "/getByGroup",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByGroup(@RequestParam Long group_id){
        try {
            Session session = sessionService.getSessionByGroup(group_id);
            return ResponseEntity.status(HttpStatus.FOUND).body(session);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping(value = "/allByMarkAsc",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<?> getAllByMarkAsc(){
        return sessionService.getSessionsSortedByFieldMarkAsc();
    }
    @GetMapping(value = "/allByMarkDesc",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<?> getAllByMarkDesc(){
        return sessionService.getSessionsSortedByFieldMarkDesc();
    }

    @GetMapping(value = "/getAllByType",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllByType(@RequestParam String type){
        try {
            List<Session> sessions = sessionService.getSessionByType(type);
            return ResponseEntity.status(HttpStatus.FOUND).body(sessions);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping(value = "/allByDayAndTimeAsc",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> allByDayAndTimeAsc(@RequestParam DayOfWeek dayOfWeek){
        try {
            List<Session> sessions = sessionService.getSessionByDayAndTime(dayOfWeek);
            return ResponseEntity.status(HttpStatus.FOUND).body(sessions);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(value = "/getByDayAndField",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByDayAndField(@RequestParam DayOfWeek dayOfWeek, @RequestParam Long id_field){
        try {
            List<Session> sessions = sessionService.allByDayAndField(dayOfWeek,id_field);
            return ResponseEntity.status(HttpStatus.FOUND).body(sessions);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping(value = "/getByTypeOfGroup",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSessionsByTypeOfGroup(@RequestParam String type){
        try {
            List<Session> sessions = sessionService.getByGroupType(type);
            return ResponseEntity.status(HttpStatus.OK).body(sessions);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}




