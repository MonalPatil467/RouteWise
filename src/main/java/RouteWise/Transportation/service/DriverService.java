package RouteWise.Transportation.service;

import RouteWise.Transportation.dtos.AuthResponseDTO;
import RouteWise.Transportation.dtos.DriverResponseDTO;
import RouteWise.Transportation.dtos.DriverSignupDTO;
import RouteWise.Transportation.dtos.LoginRequestDTO;

import java.util.List;

public interface DriverService {
    AuthResponseDTO signup(DriverSignupDTO dto);

    List<DriverResponseDTO> getAvailableDrivers(String pickUpLocation);

    AuthResponseDTO login(LoginRequestDTO dto);
}
