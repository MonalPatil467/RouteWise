package RouteWise.Transportation.Entities;

import RouteWise.Transportation.Enums.RideStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long driverId;

    private String pickupLocation;

    private String dropLocation;

    private String goodsType;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private RideStatus status;


    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double dropLatitude;

    private Double dropLongitude;

    // NEW - latest known driver location for this ride
    private Double driverLatitude;

    private Double driverLongitude;

    private Double goodsWeightKg;

    private Integer goodsQuantity;

    private Boolean fragile;

    private Boolean perishable;

    private Boolean requiresCoveredVehicle;

    private Boolean requiresRefrigeration;

    private Boolean requiresLoading;

    private Boolean requiresUnloading;

    private Double goodsWeight;
}
