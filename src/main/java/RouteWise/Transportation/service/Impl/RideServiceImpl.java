package RouteWise.Transportation.service.Impl;

import RouteWise.Transportation.AI.AIRecommendationService;
import RouteWise.Transportation.Entities.Driver;
import RouteWise.Transportation.Entities.Ride;
import RouteWise.Transportation.Enums.RideStatus;
import RouteWise.Transportation.Repositories.DriverRepository;
import RouteWise.Transportation.Repositories.RideRepository;
import RouteWise.Transportation.dtos.*;
import RouteWise.Transportation.exceptions.DriverUnavailableException;
import RouteWise.Transportation.mappers.DriverMapper;
import RouteWise.Transportation.mappers.RideMapper;
import RouteWise.Transportation.service.RideService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final AIRecommendationService aiRecommendationService;
    private final RideMapper rideMapper;


    // =========================================================
    // SEARCH AVAILABLE DRIVERS
    // =========================================================

    @Override
    public DriverSearchResponseDTO searchDrivers(
            SearchRideDTO dto
    ) {

        if (dto == null) {
            throw new RuntimeException(
                    "Search request cannot be null"
            );
        }

        if (dto.getVehicleType() == null) {
            throw new RuntimeException(
                    "Vehicle type is required"
            );
        }

        if (dto.getPickupLocation() == null ||
                dto.getPickupLocation().isBlank()) {

            throw new RuntimeException(
                    "Pickup location is required"
            );
        }

        if (dto.getPickupLatitude() == null ||
                dto.getPickupLongitude() == null) {

            throw new RuntimeException(
                    "Pickup latitude and longitude are required"
            );
        }

        if (dto.getGoodsWeight() == null ||
                dto.getGoodsWeight() <= 0) {

            throw new RuntimeException(
                    "Goods weight must be greater than 0 kg"
            );
        }

        validateCoordinates(
                dto.getPickupLatitude(),
                dto.getPickupLongitude()
        );

        /*
         * Repository filters:
         *
         * 1. Correct vehicle type
         * 2. Driver available
         * 3. Vehicle capacity >= goods weight
         * 4. Valid GPS coordinates
         *
         * Drivers are sorted by distance.
         */
        List<Driver> drivers =
                driverRepository.findNearbyDrivers(
                        dto.getVehicleType(),
                        dto.getPickupLatitude(),
                        dto.getPickupLongitude(),
                        dto.getGoodsWeight()
                );

        StringBuilder driverData =
                new StringBuilder();

        for (Driver driver : drivers) {

            driverData.append("Driver Name: ")
                    .append(driver.getName())
                    .append(", Price: ")
                    .append(driver.getPrice())
                    .append(", Vehicle: ")
                    .append(driver.getVehicleType())
                    .append(", Capacity: ")
                    .append(driver.getCapacityKg())
                    .append(" kg")
                    .append(", Rating: ")
                    .append(driver.getRating())
                    .append(", Latitude: ")
                    .append(driver.getLatitude())
                    .append(", Longitude: ")
                    .append(driver.getLongitude())
                    .append("\n");
        }

        String recommendation;

        if (drivers.isEmpty()) {

            recommendation =
                    "No suitable drivers are currently available.";

        } else {

            recommendation =
                    aiRecommendationService.recommendDriver(
                            driverData.toString()
                    );
        }

        DriverSearchResponseDTO response =
                new DriverSearchResponseDTO();

        response.setDrivers(
                drivers.stream()
                        .map(driverMapper::toDTO)
                        .toList()
        );

        response.setAiRecommendation(
                recommendation
        );

        return response;
    }


    // =========================================================
    // BOOK RIDE WITH SELECTED DRIVER
    // =========================================================

    @Override
    @Transactional
    public RideResponseDTO bookRide(
            BookRideDTO dto
    ) {

        validateBookingRequest(dto);

        Driver driver =
                driverRepository
                        .findById(dto.getDriverId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                ));

        /*
         * Double-check availability before booking.
         *
         * A driver may have become unavailable after
         * the search request was made.
         */
        if (!Boolean.TRUE.equals(driver.getAvailable())) {

            throw new DriverUnavailableException(
                    "Driver is currently unavailable"
            );
        }

        /*
         * Double-check vehicle capacity.
         *
         * This prevents a customer from manually selecting
         * a vehicle that cannot carry the requested goods.
         */
        if (driver.getCapacityKg() == null) {

            throw new RuntimeException(
                    "Driver vehicle capacity is not configured"
            );
        }

        if (dto.getGoodsWeight() > driver.getCapacityKg()) {

            throw new RuntimeException(
                    "Driver vehicle capacity is insufficient. " +
                            "Vehicle capacity: " +
                            driver.getCapacityKg() +
                            " kg, Goods weight: " +
                            dto.getGoodsWeight() +
                            " kg"
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

        ride.setGoodsWeight(
                dto.getGoodsWeight()
        );

        ride.setUserId(
                dto.getUserId()
        );

        ride.setDriverId(
                driver.getId()
        );

        ride.setAmount(
                driver.getPrice()
        );

        ride.setStatus(
                RideStatus.BOOKED
        );

        ride.setPickupLatitude(
                dto.getPickupLatitude()
        );

        ride.setPickupLongitude(
                dto.getPickupLongitude()
        );

        ride.setDropLatitude(
                dto.getDropLatitude()
        );

        ride.setDropLongitude(
                dto.getDropLongitude()
        );

        ride.setDriverLatitude(
                driver.getLatitude()
        );

        ride.setDriverLongitude(
                driver.getLongitude()
        );

        /*
         * Driver is no longer available for another
         * ride request once this ride has been booked.
         */
        driver.setAvailable(false);

        driverRepository.save(driver);

        Ride savedRide =
                rideRepository.save(ride);

        return rideMapper.toDTO(savedRide);
    }


    // =========================================================
    // AUTOMATICALLY BOOK BEST AVAILABLE DRIVER
    // =========================================================

    @Override
    @Transactional
    public RideResponseDTO bookBestAvailableRide(
            AutoBookRideDTO dto
    ) {

        validateAutoBookingRequest(dto);

        /*
         * Repository returns only:
         *
         * - requested vehicle type
         * - available drivers
         * - sufficient vehicle capacity
         * - drivers with GPS coordinates
         *
         * Results are ordered by distance.
         */
        List<Driver> drivers =
                driverRepository.findNearbyDrivers(
                        dto.getVehicleType(),
                        dto.getPickupLatitude(),
                        dto.getPickupLongitude(),
                        dto.getGoodsWeight()
                );

        if (drivers.isEmpty()) {

            throw new DriverUnavailableException(
                    "No available driver with sufficient vehicle capacity found near pickup location"
            );
        }

        /*
         * First driver is currently the nearest suitable driver.
         */
        Driver bestDriver =
                drivers.get(0);

        BookRideDTO bookRideDTO =
                new BookRideDTO();

        bookRideDTO.setUserId(
                dto.getUserId()
        );

        bookRideDTO.setDriverId(
                bestDriver.getId()
        );

        bookRideDTO.setPickupLocation(
                dto.getPickupLocation()
        );

        bookRideDTO.setDropLocation(
                dto.getDropLocation()
        );

        bookRideDTO.setGoodsType(
                dto.getGoodsType()
        );

        bookRideDTO.setGoodsWeight(
                dto.getGoodsWeight()
        );

        bookRideDTO.setPickupLatitude(
                dto.getPickupLatitude()
        );

        bookRideDTO.setPickupLongitude(
                dto.getPickupLongitude()
        );

        bookRideDTO.setDropLatitude(
                dto.getDropLatitude()
        );

        bookRideDTO.setDropLongitude(
                dto.getDropLongitude()
        );

        return bookRide(bookRideDTO);
    }


    // =========================================================
    // DRIVER ACCEPTS RIDE
    // =========================================================

    @Override
    @Transactional
    public RideResponseDTO acceptRide(
            Long rideId
    ) {

        Ride ride =
                findRide(rideId);

        /*
         * Only BOOKED rides can be accepted.
         */
        if (ride.getStatus() != RideStatus.BOOKED) {

            throw new RuntimeException(
                    "Only BOOKED rides can be accepted"
            );
        }

        Driver driver =
                driverRepository
                        .findById(ride.getDriverId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                ));

        /*
         * Driver must still be available.
         */
        if (!Boolean.TRUE.equals(driver.getAvailable())) {

            throw new DriverUnavailableException(
                    "Driver is currently unavailable"
            );
        }

        /*
         * Driver accepts the ride.
         */
        ride.setStatus(
                RideStatus.ACCEPTED
        );

        /*
         * Driver remains unavailable while handling
         * the current ride.
         */
        driver.setAvailable(false);

        driverRepository.save(driver);

        Ride updatedRide =
                rideRepository.save(ride);

        return rideMapper.toDTO(updatedRide);
    }


    // =========================================================
    // GET RIDE
    // =========================================================

    @Override
    public RideResponseDTO getRide(
            Long rideId
    ) {

        Ride ride =
                findRide(rideId);

        return rideMapper.toDTO(ride);
    }


    // =========================================================
    // GET RIDE BY ID
    // =========================================================

    @Override
    public RideResponseDTO getRideById(
            Long rideId
    ) {

        Ride ride =
                findRide(rideId);

        return rideMapper.toDTO(ride);
    }


    // =========================================================
    // GET ALL RIDES
    // =========================================================

    @Override
    public List<RideResponseDTO> getAllRides() {

        return rideRepository
                .findAll()
                .stream()
                .map(rideMapper::toDTO)
                .toList();
    }


    // =========================================================
    // GET RIDES BY USER
    // =========================================================

    @Override
    public List<RideResponseDTO> getRidesByUser(
            Long userId
    ) {

        if (userId == null) {

            throw new RuntimeException(
                    "User ID is required"
            );
        }

        return rideRepository
                .findByUserId(userId)
                .stream()
                .map(rideMapper::toDTO)
                .toList();
    }


    // =========================================================
    // GET RIDES BY DRIVER
    // =========================================================

    @Override
    public List<RideResponseDTO> getRidesByDriver(
            Long driverId
    ) {

        if (driverId == null) {

            throw new RuntimeException(
                    "Driver ID is required"
            );
        }

        return rideRepository
                .findByDriverId(driverId)
                .stream()
                .map(rideMapper::toDTO)
                .toList();
    }


    // =========================================================
    // UPDATE RIDE STATUS
    // =========================================================

    @Override
    @Transactional
    public RideResponseDTO updateRideStatus(
            Long rideId,
            String status
    ) {

        Ride ride =
                findRide(rideId);

        if (status == null ||
                status.isBlank()) {

            throw new RuntimeException(
                    "Ride status is required"
            );
        }

        RideStatus newStatus;

        try {

            newStatus =
                    RideStatus.valueOf(
                            status.trim()
                                    .toUpperCase()
                    );

        } catch (IllegalArgumentException e) {

            throw new RuntimeException(
                    "Invalid ride status: " + status
            );
        }

        /*
         * Make sure the ride follows the correct lifecycle.
         */
        validateStatusTransition(
                ride.getStatus(),
                newStatus
        );

        ride.setStatus(newStatus);

        /*
         * Once the goods are delivered:
         *
         * 1. Driver becomes available again.
         * 2. Completed ride count increases.
         */
        if (newStatus == RideStatus.DELIVERED) {

            Driver driver =
                    driverRepository
                            .findById(
                                    ride.getDriverId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Driver not found"
                                    )
                            );

            driver.setAvailable(true);

            /*
             * Null-safe increment.
             */
            int completedRides =
                    driver.getCompletedRides() == null
                            ? 0
                            : driver.getCompletedRides();

            driver.setCompletedRides(
                    completedRides + 1
            );

            driverRepository.save(driver);
        }

        Ride updatedRide =
                rideRepository.save(ride);

        return rideMapper.toDTO(updatedRide);
    }


    // =========================================================
    // GET CURRENT DRIVER LOCATION
    // =========================================================

    @Override
    public RideLocationResponseDTO getRideLocation(
            Long rideId
    ) {

        Ride ride =
                findRide(rideId);

        if (ride.getDriverId() == null) {

            throw new RuntimeException(
                    "No driver assigned to this ride"
            );
        }

        Driver driver =
                driverRepository
                        .findById(
                                ride.getDriverId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                ));

        RideLocationResponseDTO response =
                new RideLocationResponseDTO();

        response.setRideId(
                ride.getId()
        );

        response.setDriverId(
                driver.getId()
        );

        response.setDriverName(
                driver.getName()
        );

        response.setLatitude(
                driver.getLatitude()
        );

        response.setLongitude(
                driver.getLongitude()
        );

        response.setCurrentLocation(
                driver.getCurrentLocation()
        );

        return response;
    }


    // =========================================================
    // FIND RIDE
    // =========================================================

    private Ride findRide(
            Long rideId
    ) {

        if (rideId == null) {

            throw new RuntimeException(
                    "Ride ID is required"
            );
        }

        return rideRepository
                .findById(rideId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ride not found"
                        ));
    }


    // =========================================================
    // VALIDATE BOOKING REQUEST
    // =========================================================

    private void validateBookingRequest(
            BookRideDTO dto
    ) {

        if (dto == null) {

            throw new RuntimeException(
                    "Booking request cannot be null"
            );
        }

        if (dto.getUserId() == null) {

            throw new RuntimeException(
                    "User ID is required"
            );
        }

        if (dto.getDriverId() == null) {

            throw new RuntimeException(
                    "Driver ID is required"
            );
        }

        if (dto.getPickupLocation() == null ||
                dto.getPickupLocation().isBlank()) {

            throw new RuntimeException(
                    "Pickup location is required"
            );
        }

        if (dto.getDropLocation() == null ||
                dto.getDropLocation().isBlank()) {

            throw new RuntimeException(
                    "Drop location is required"
            );
        }

        if (dto.getGoodsType() == null ||
                dto.getGoodsType().isBlank()) {

            throw new RuntimeException(
                    "Goods type is required"
            );
        }

        if (dto.getGoodsWeight() == null ||
                dto.getGoodsWeight() <= 0) {

            throw new RuntimeException(
                    "Goods weight must be greater than 0 kg"
            );
        }

        if (dto.getPickupLocation()
                .trim()
                .equalsIgnoreCase(
                        dto.getDropLocation().trim()
                )) {

            throw new RuntimeException(
                    "Pickup and drop locations cannot be the same"
            );
        }

        if (dto.getPickupLatitude() == null ||
                dto.getPickupLongitude() == null) {

            throw new RuntimeException(
                    "Pickup coordinates are required"
            );
        }

        if (dto.getDropLatitude() == null ||
                dto.getDropLongitude() == null) {

            throw new RuntimeException(
                    "Drop coordinates are required"
            );
        }

        validateCoordinates(
                dto.getPickupLatitude(),
                dto.getPickupLongitude()
        );

        validateCoordinates(
                dto.getDropLatitude(),
                dto.getDropLongitude()
        );
    }


    // =========================================================
    // VALIDATE AUTO BOOKING REQUEST
    // =========================================================

    private void validateAutoBookingRequest(
            AutoBookRideDTO dto
    ) {

        if (dto == null) {

            throw new RuntimeException(
                    "Auto booking request cannot be null"
            );
        }

        if (dto.getUserId() == null) {

            throw new RuntimeException(
                    "User ID is required"
            );
        }

        if (dto.getPickupLocation() == null ||
                dto.getPickupLocation().isBlank()) {

            throw new RuntimeException(
                    "Pickup location is required"
            );
        }

        if (dto.getDropLocation() == null ||
                dto.getDropLocation().isBlank()) {

            throw new RuntimeException(
                    "Drop location is required"
            );
        }

        if (dto.getGoodsType() == null ||
                dto.getGoodsType().isBlank()) {

            throw new RuntimeException(
                    "Goods type is required"
            );
        }

        if (dto.getGoodsWeight() == null ||
                dto.getGoodsWeight() <= 0) {

            throw new RuntimeException(
                    "Goods weight must be greater than 0 kg"
            );
        }

        if (dto.getVehicleType() == null) {

            throw new RuntimeException(
                    "Vehicle type is required"
            );
        }

        if (dto.getPickupLatitude() == null ||
                dto.getPickupLongitude() == null) {

            throw new RuntimeException(
                    "Pickup coordinates are required"
            );
        }

        if (dto.getDropLatitude() == null ||
                dto.getDropLongitude() == null) {

            throw new RuntimeException(
                    "Drop coordinates are required"
            );
        }

        validateCoordinates(
                dto.getPickupLatitude(),
                dto.getPickupLongitude()
        );

        validateCoordinates(
                dto.getDropLatitude(),
                dto.getDropLongitude()
        );
    }


    // =========================================================
    // VALIDATE GPS COORDINATES
    // =========================================================

    private void validateCoordinates(
            Double latitude,
            Double longitude
    ) {

        if (latitude == null ||
                longitude == null) {

            throw new RuntimeException(
                    "Coordinates are required"
            );
        }

        if (latitude < -90 ||
                latitude > 90) {

            throw new RuntimeException(
                    "Invalid latitude"
            );
        }

        if (longitude < -180 ||
                longitude > 180) {

            throw new RuntimeException(
                    "Invalid longitude"
            );
        }
    }


    // =========================================================
    // VALIDATE RIDE STATUS
    // =========================================================

    private void validateStatusTransition(
            RideStatus currentStatus,
            RideStatus newStatus
    ) {

        if (currentStatus == RideStatus.BOOKED) {

            if (newStatus != RideStatus.ACCEPTED) {

                throw new RuntimeException(
                        "BOOKED ride can only move to ACCEPTED"
                );
            }

            return;
        }

        if (currentStatus == RideStatus.ACCEPTED) {

            if (newStatus != RideStatus.IN_TRANSIT) {

                throw new RuntimeException(
                        "ACCEPTED ride can only move to IN_TRANSIT"
                );
            }

            return;
        }

        if (currentStatus == RideStatus.IN_TRANSIT) {

            if (newStatus != RideStatus.DELIVERED) {

                throw new RuntimeException(
                        "IN_TRANSIT ride can only move to DELIVERED"
                );
            }

            return;
        }

        if (currentStatus == RideStatus.DELIVERED) {

            throw new RuntimeException(
                    "DELIVERED ride cannot be updated"
            );
        }
    }
}





