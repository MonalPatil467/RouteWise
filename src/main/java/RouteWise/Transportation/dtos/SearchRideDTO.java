package RouteWise.Transportation.dtos;

import RouteWise.Transportation.Enums.VehicleType;
import lombok.Data;

@Data
public class SearchRideDTO {
    private String pickupLocation;
    private String dropLocation;
    private VehicleType vehicleType;
}
