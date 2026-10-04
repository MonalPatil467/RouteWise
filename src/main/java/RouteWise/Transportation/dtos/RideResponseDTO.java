package RouteWise.Transportation.dtos;

import RouteWise.Transportation.Enums.RideStatus;
import lombok.Data;

@Data
public class RideResponseDTO {

    private Long id;

    private Long userId;

    private Long driverId;

    private String pickupLocation;

    private String dropLocation;

    private String goodsType;

    private Double amount;

    private RideStatus status;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double dropLatitude;

    private Double dropLongitude;

    private Double driverLatitude;

    private Double driverLongitude;
}