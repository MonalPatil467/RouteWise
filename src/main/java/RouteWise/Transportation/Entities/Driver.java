package RouteWise.Transportation.Entities;

import RouteWise.Transportation.Enums.VehicleType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="driver")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String phone;

    private String password;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private Boolean available;

    private Double price;

    private String currentLocation;

    private Double rating;

    private Integer completedRides;
}
