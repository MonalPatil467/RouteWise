
package RouteWise.Transportation.dtos;

import RouteWise.Transportation.Enums.VehicleType;
import lombok.Data;

@Data
public class SearchRideDTO {

    private VehicleType vehicleType;

    private String pickupLocation;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double goodsWeight;
}