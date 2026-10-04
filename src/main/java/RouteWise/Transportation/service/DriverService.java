package RouteWise.Transportation.service;

import RouteWise.Transportation.dtos.DriverAvailabilityResponseDTO;
import RouteWise.Transportation.dtos.DriverProfileDTO;
import RouteWise.Transportation.dtos.DriverResponseDTO;

import java.util.List;

public interface DriverService {

    // Get available drivers
    List<DriverResponseDTO> getAvailableDrivers(
            String pickUpLocation
    );

    // Update driver availability
    DriverAvailabilityResponseDTO updateAvailability(
            String phone,
            Boolean available
    );

    // Get driver profile
    DriverProfileDTO getProfile(
            String phone
    );

    // Update driver profile
    DriverProfileDTO updateProfile(
            String phone,
            DriverProfileDTO dto
    );
}

