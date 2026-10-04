package RouteWise.Transportation.service;

import RouteWise.Transportation.dtos.*;

import java.util.List;

public interface RideService {

    DriverSearchResponseDTO searchDrivers(
            SearchRideDTO dto
    );

    RideResponseDTO bookRide(
            BookRideDTO dto
    );

    RideResponseDTO bookBestAvailableRide(
            AutoBookRideDTO dto
    );

    RideResponseDTO acceptRide(
            Long rideId
    );

    RideResponseDTO getRide(
            Long rideId
    );

    RideResponseDTO getRideById(
            Long rideId
    );

    List<RideResponseDTO> getAllRides();

    List<RideResponseDTO> getRidesByUser(
            Long userId
    );

    List<RideResponseDTO> getRidesByDriver(
            Long driverId
    );

    RideResponseDTO updateRideStatus(
            Long rideId,
            String status
    );

    RideLocationResponseDTO getRideLocation(
            Long rideId
    );
}