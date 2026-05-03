package RouteWise.Transportation.controller;

import RouteWise.Transportation.dtos.BookRideDTO;
import RouteWise.Transportation.dtos.DriverResponseDTO;
import RouteWise.Transportation.dtos.RideResponseDTO;
import RouteWise.Transportation.dtos.SearchRideDTO;
import RouteWise.Transportation.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ride")
public class RideController {
    private final RideService rideService;

    // SEARCH AVAILABLE DRIVERS
    @PostMapping("/search")
    public List<DriverResponseDTO> searchDrivers(
            @RequestBody SearchRideDTO dto
    ) {

        return rideService.searchDrivers(dto);
    }

    // BOOK RIDE
    @PostMapping("/book")
    public RideResponseDTO bookRide(
            @RequestBody BookRideDTO dto
    ) {

        return rideService.bookRide(dto);
    }

    // DRIVER ACCEPTS RIDE
    @PutMapping("/accept/{rideId}")
    public RideResponseDTO acceptRide(
            @PathVariable Long rideId
    ) {

        return rideService.acceptRide(rideId);
    }

    // GET SINGLE RIDE
    @GetMapping("/{rideId}")
    public RideResponseDTO getRide(
            @PathVariable Long rideId
    ) {

        return rideService.getRide(rideId);
    }

    // GET ALL RIDES
    @GetMapping("/all")
    public List<RideResponseDTO> getAllRides() {

        return rideService.getAllRides();
    }

    // USER RIDE HISTORY
    @GetMapping("/user/{userId}")
    public List<RideResponseDTO> getRidesByUser(
            @PathVariable Long userId
    ) {

        return rideService.getRidesByUser(userId);
    }

    // DRIVER RIDE HISTORY
    @GetMapping("/driver/{driverId}")
    public List<RideResponseDTO> getRidesByDriver(
            @PathVariable Long driverId
    ) {

        return rideService.getRidesByDriver(driverId);
    }

    // UPDATE RIDE STATUS
    @PutMapping("/status/{rideId}")
    public RideResponseDTO updateRideStatus(
            @PathVariable Long rideId,
            @RequestParam String status
    ) {

        return rideService.updateRideStatus(
                rideId,
                status
        );
    }
}
