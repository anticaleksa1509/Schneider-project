package raf.rs.SportsBooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import raf.rs.SportsBooking.model.Field;
import raf.rs.SportsBooking.repository.FieldRepo;

import java.util.List;
import java.util.Optional;

@Service
public class FieldService {

    @Autowired
    FieldRepo fieldRepo;

    public void createNewField(Field field) throws Exception {

        if(field.getCapacity() < 10 || field.getCapacity() > 30) {
            throw new Exception("Field capacity must be between 10 and 30");
        }
        if(fieldRepo.existsByMark(field.getMark()))
            throw new IllegalArgumentException("Field with mark already exist");
        fieldRepo.save(field);


    }

    public Field getFieldByMark(String mark) throws Exception{
        Optional<Field> optionalField = fieldRepo.findByMark(mark);
        if(optionalField.isPresent()) {
            Field field = optionalField.get();
            return field;
        }else {
            throw new Exception("Field with entered mark does not exist");
        }


    }

}
