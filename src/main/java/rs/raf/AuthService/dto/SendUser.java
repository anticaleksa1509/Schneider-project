package rs.raf.AuthService.dto;

import lombok.Data;

@Data
public class SendUser {

    private String name;
    private String lastName;
    private String email;
    private String areaOfExpertise;
    private String coachingTitle;

}
