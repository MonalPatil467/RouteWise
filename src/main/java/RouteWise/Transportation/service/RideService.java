package RouteWise.Transportation.service;

import RouteWise.Transportation.dtos.BookRideDTO;
import RouteWise.Transportation.dtos.DriverResponseDTO;
import RouteWise.Transportation.dtos.RideResponseDTO;
import RouteWise.Transportation.dtos.SearchRideDTO;

import java.util.List;

public interface RideService {
    List<DriverResponseDTO> searchDrivers(SearchRideDTO dto);

    RideResponseDTO bookRide(BookRideDTO dto);

    RideResponseDTO acceptRide(Long rideId);

    RideResponseDTO getRide(Long rideId);

    RideResponseDTO getRideById(Long rideId);

    List<RideResponseDTO> getAllRides();

    List<RideResponseDTO> getRidesByUser(Long userId);

    List<RideResponseDTO> getRidesByDriver(Long driverId);

    RideResponseDTO updateRideStatus(
            Long rideId,
            String status
    );
}
