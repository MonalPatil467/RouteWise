package RouteWise.Transportation.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;

    @Column(unique = true)
    private String phone;

    private String password;

}
