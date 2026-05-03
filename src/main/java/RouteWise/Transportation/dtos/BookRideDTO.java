package RouteWise.Transportation.dtos;

import lombok.Data;

@Data
public class BookRideDTO {
    private Long userId;
    private Long driverId;

    private String pickupLocation;
    private String dropLocation;

    private String goodsType;
}
