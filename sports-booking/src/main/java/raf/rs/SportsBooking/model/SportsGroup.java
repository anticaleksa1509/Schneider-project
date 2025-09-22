package raf.rs.SportsBooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class SportsGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_group;
    private String markGroup;
    private String typeOfSport;
    private int maxMembers;
    private String type;

    @JsonIgnore
    @OneToMany(mappedBy = "sportsGroup")
    private List<Session> sessions;

}
