package RouteWise.Transportation.Entities;

import RouteWise.Transportation.Enums.RideStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="ride")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pickupLocation;

    private String dropLocation;

    private String goodsType;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private Long userId;

    private Long driverId;
}
