package RouteWise.Transportation.dtos;

import RouteWise.Transportation.Enums.VehicleType;
import lombok.Data;

@Data
public class DriverSignupDTO {
    private String name;
    private String phone;
    private String password;
    private VehicleType vehicleType;
}
