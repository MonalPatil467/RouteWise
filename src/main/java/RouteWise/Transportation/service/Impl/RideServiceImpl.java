package RouteWise.Transportation.service.Impl;

import RouteWise.Transportation.Entities.Driver;
import RouteWise.Transportation.Entities.Ride;
import RouteWise.Transportation.Enums.RideStatus;
import RouteWise.Transportation.Repositories.DriverRepository;
import RouteWise.Transportation.Repositories.RideRepository;
import RouteWise.Transportation.dtos.BookRideDTO;
import RouteWise.Transportation.dtos.DriverResponseDTO;
import RouteWise.Transportation.dtos.RideResponseDTO;
import RouteWise.Transportation.dtos.SearchRideDTO;
import RouteWise.Transportation.mappers.DriverMapper;
import RouteWise.Transportation.mappers.RideMapper;
import RouteWise.Transportation.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private final RideRepository rideRepository;

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    private final RideMapper rideMapper;

    @Override
    public List<DriverResponseDTO> searchDrivers(
            SearchRideDTO dto
    ) {

        List<Driver> drivers =
                driverRepository
                        .findByVehicleTypeAndAvailableAndCurrentLocation(
                                dto.getVehicleType(),
                                true,
                                dto.getPickupLocation()
                        );

        return drivers
                .stream()
                .map(driverMapper::toDTO)
                .toList();
    }

    @Override
    public RideResponseDTO bookRide(
            BookRideDTO dto
    ) {

        Driver driver =
                driverRepository.findById(dto.getDriverId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                ));

        if (!driver.getAvailable()) {

            throw new RuntimeException(
                    "Driver not available"
            );
        }

        Ride ride = new Ride();

        ride.setPickupLocation(
                dto.getPickupLocation()
        );

        ride.setDropLocation(
                dto.getDropLocation()
        );

        ride.setGoodsType(
                dto.getGoodsType()
        );

        ride.setAmount(
                driver.getPrice()
        );

        ride.setUserId(
                dto.getUserId()
        );

        ride.setDriverId(
                dto.getDriverId()
        );

        ride.setStatus(
                RideStatus.BOOKED
        );

        Ride savedRide =
                rideRepository.save(ride);

        return rideMapper.toDTO(savedRide);
    }

    @Override
    public RideResponseDTO acceptRide(
            Long rideId
    ) {

        Ride ride =
                rideRepository.findById(rideId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ride not found"
                                ));

        ride.setStatus(
                RideStatus.ACCEPTED
        );

        Ride updatedRide =
                rideRepository.save(ride);

        return rideMapper.toDTO(updatedRide);
    }

    @Override
    public RideResponseDTO getRide(
            Long rideId
    ) {

        Ride ride =
                rideRepository.findById(rideId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ride not found"
                                ));

        return rideMapper.toDTO(ride);
    }

    @Override
    public RideResponseDTO getRideById(
            Long rideId
    ) {

        Ride ride =
                rideRepository.findById(rideId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ride not found"
                                ));

        return rideMapper.toDTO(ride);
    }

    @Override
    public List<RideResponseDTO> getAllRides() {

        return rideRepository.findAll()
                .stream()
                .map(rideMapper::toDTO)
                .toList();
    }

    @Override
    public List<RideResponseDTO> getRidesByUser(
            Long userId
    ) {

        return rideRepository.findByUserId(userId)
                .stream()
                .map(rideMapper::toDTO)
                .toList();
    }

    @Override
    public List<RideResponseDTO> getRidesByDriver(
            Long driverId
    ) {

        return rideRepository.findByDriverId(driverId)
                .stream()
                .map(rideMapper::toDTO)
                .toList();
    }

    @Override
    public RideResponseDTO updateRideStatus(
            Long rideId,
            String status
    ) {

        Ride ride =
                rideRepository.findById(rideId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ride not found"
                                ));

        ride.setStatus(
                RideStatus.valueOf(
                        status.toUpperCase()
                )
        );

        Ride updatedRide =
                rideRepository.save(ride);

        return rideMapper.toDTO(updatedRide);
    }

    private double calculateScore(Driver driver) {

        double score = 0;

        if(driver.getAvailable()) {
            score += 50;
        }

        score += (10000 - driver.getPrice()) / 100;

        return score;
    }
}
