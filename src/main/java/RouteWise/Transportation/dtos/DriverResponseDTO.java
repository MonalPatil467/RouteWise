package RouteWise.Transportation.dtos;

import RouteWise.Transportation.Enums.VehicleType;
import lombok.Data;

@Data
public class DriverResponseDTO {
    private Long driverId;
    private String driverName;
    private VehicleType vehicleType;
    private Double price;
}
