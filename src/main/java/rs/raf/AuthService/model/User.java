package rs.raf.AuthService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    @NotBlank//ne moze da se kreira uz Spring validaciju
    @Column(nullable = false)//a ovde baza ne dozvoljava da bude null
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank(message = "Email is mandatory!")
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<Permissions> permissions = new HashSet<>();


}
