package raf.rs.SportsBooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

@Data
@Entity
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coach_id;
    private String name;

    private String lastName;
    
    private String areaOfExpertise;
    private String coachingTitle;

    @NotBlank(message = "Email is mandatory!")
    private String email;

    @JsonIgnore
    @OneToMany(mappedBy = "coach")
    private List<Session> sessions;


}
