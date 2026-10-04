package RouteWise.Transportation.controller;

import RouteWise.Transportation.dtos.DriverAvailabilityRequest;
import RouteWise.Transportation.dtos.DriverAvailabilityResponseDTO;
import RouteWise.Transportation.dtos.DriverResponseDTO;
import RouteWise.Transportation.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver")
public class DriverController {

    private final DriverService driverService;

    @PutMapping("/availability")
    public DriverAvailabilityResponseDTO updateAvailability(
            @RequestBody DriverAvailabilityRequest request,
            Authentication authentication
    ) {
        return driverService.updateAvailability(
                authentication.getName(),
                request.getAvailable()
        );
    }
    @GetMapping("/profile")
    public DriverResponseDTO getProfile(
            Authentication authentication
    ) {
        return driverService.getProfile(authentication.getName());
    }

    @PutMapping("/profile")
    public DriverResponseDTO updateProfile(
            @RequestBody DriverProfileDTO dto,
            Authentication authentication
    ) {
        return driverService.updateProfile(
                authentication.getName(),
                dto
        );
    }
}

