package raf.rs.SportsBooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long field_id;

    @NotBlank
    private String type;
    @NotBlank
    private String mark;
    //@Size(min = 10,max = 30)
    private int capacity;
    @NotBlank
    private String outsideOrInside;

    @JsonIgnore
    @OneToMany(mappedBy = "field")
    private List<Session> sessions;

}
