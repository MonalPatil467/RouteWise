package RouteWise.Transportation.Entities;

import RouteWise.Transportation.Enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "driver")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    // Maximum goods weight this vehicle can carry
    private Double capacityKg;

    private Double price;

    private Double rating;

    private Integer completedRides;

    private Boolean available;

    // Keep this for displaying the location name
    private String currentLocation;

    // Actual GPS coordinates
    private Double latitude;

    private Double longitude;
}

