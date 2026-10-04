package RouteWise.Transportation.dtos;

import lombok.Data;

@Data
public class BookRideDTO {

    private Long userId;

    private Long driverId;

    private String pickupLocation;

    private String dropLocation;

    private String goodsType;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double dropLatitude;

    private Double dropLongitude;

    private Double goodsWeight;
}