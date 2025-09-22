package raf.rs.SportsBooking.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raf.rs.SportsBooking.model.Field;
import raf.rs.SportsBooking.service.FieldService;

import java.awt.*;

@RestController
@RequestMapping("/api/fields")
public class FieldController {

    @Autowired
    FieldService fieldService;

    @PostMapping(value = "/createField",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createNewField(@Valid @RequestBody Field field){
        try{
            fieldService.createNewField(field);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully created!");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
    @GetMapping(value = "/getByMark",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFieldByMark(@RequestParam String mark){
        try{
            Field field = fieldService.getFieldByMark(mark);
            return ResponseEntity.status(HttpStatus.FOUND).body(field);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
