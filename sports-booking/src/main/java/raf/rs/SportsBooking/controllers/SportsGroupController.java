package raf.rs.SportsBooking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.rs.SportsBooking.model.SportsGroup;
import raf.rs.SportsBooking.service.SportsGroupService;

import java.util.List;

@RestController
@RequestMapping("/api/sportsGroup")
public class SportsGroupController {

    @Autowired
    SportsGroupService sportsGroupService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createGroup(@RequestBody SportsGroup sportsGroup){
        try {
            String message = sportsGroupService.createGroup(sportsGroup);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping(value ="/getByTypeOfSport",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getByTypeOfSport(@RequestParam String typeOfSport){
        try{
            List<SportsGroup> sportsGroups = sportsGroupService.getByTypeOfSport(typeOfSport);
            return ResponseEntity.status(HttpStatus.FOUND).body(sportsGroups);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
