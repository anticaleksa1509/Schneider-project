package raf.rs.SportsBooking.controllers;


import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raf.rs.SportsBooking.model.Coach;
import raf.rs.SportsBooking.repository.CoachRepo;
import raf.rs.SportsBooking.service.CoachService;

import java.util.List;

@RestController
@RequestMapping("/api/coach")
public class CoachController {

    @Autowired
    CoachService coachService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createCoach(@Valid @RequestBody Coach coach) {
        try {
            String message = coachService.createCoach(coach);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping(value = "getByNameAndLastName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByNameAndLastName(@RequestParam String name,@RequestParam String lastName){
        try {
            List<Coach> coaches = coachService.getByNameAndLastName(name,lastName);
            return ResponseEntity.status(HttpStatus.FOUND).body(coaches);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
}
