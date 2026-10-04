package RouteWise.Transportation.service.Impl;

import RouteWise.Transportation.Entities.Driver;
import RouteWise.Transportation.Repositories.DriverRepository;
import RouteWise.Transportation.dtos.*;
import RouteWise.Transportation.mappers.DriverMapper;
import RouteWise.Transportation.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;



    @Override
    public List<DriverResponseDTO> getAvailableDrivers(
            String pickUpLocation
    ) {

        if (pickUpLocation == null ||
                pickUpLocation.isBlank()) {

            throw new RuntimeException(
                    "Pickup location is required"
            );
        }

        List<Driver> drivers =
                driverRepository
                        .findByAvailableTrueAndCurrentLocation(
                                pickUpLocation
                        );

        return drivers.stream()
                .map(driverMapper::toDTO)
                .toList();
    }



    @Override
    public DriverAvailabilityResponseDTO updateAvailability(
            String phone,
            Boolean available
    ) {

        if (phone == null ||
                phone.isBlank()) {

            throw new RuntimeException(
                    "Driver phone is required"
            );
        }

        if (available == null) {

            throw new RuntimeException(
                    "Availability status is required"
            );
        }

        Driver driver =
                driverRepository
                        .findByPhone(phone)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                )
                        );

        driver.setAvailable(available);

        Driver savedDriver =
                driverRepository.save(driver);

        DriverAvailabilityResponseDTO response =
                new DriverAvailabilityResponseDTO();

        response.setDriverId(
                savedDriver.getId()
        );

        response.setName(
                savedDriver.getName()
        );

        response.setAvailable(
                savedDriver.getAvailable()
        );

        if (Boolean.TRUE.equals(
                savedDriver.getAvailable())) {

            response.setMessage(
                    "Driver is now available for ride requests"
            );

        } else {

            response.setMessage(
                    "Driver is now unavailable. No new ride requests will be sent"
            );
        }

        return response;
    }



    @Override
    public DriverProfileDTO getProfile(
            String phone
    ) {

        if (phone == null ||
                phone.isBlank()) {

            throw new RuntimeException(
                    "Driver phone is required"
            );
        }

        Driver driver =
                driverRepository
                        .findByPhone(phone)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                )
                        );

        return convertToProfileDTO(driver);
    }


    @Override
    public DriverProfileDTO updateProfile(
            String phone,
            DriverProfileDTO dto
    ) {

        if (phone == null ||
                phone.isBlank()) {

            throw new RuntimeException(
                    "Driver phone is required"
            );
        }

        if (dto == null) {

            throw new RuntimeException(
                    "Driver profile data is required"
            );
        }

        Driver driver =
                driverRepository
                        .findByPhone(phone)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Driver not found"
                                )
                        );



        if (dto.getName() != null &&
                !dto.getName().isBlank()) {

            driver.setName(
                    dto.getName().trim()
            );
        }



        if (dto.getPhone() != null &&
                !dto.getPhone().isBlank() &&
                !phone.equals(dto.getPhone())) {

            if (driverRepository.existsByPhone(
                    dto.getPhone())) {

                throw new RuntimeException(
                        "Phone number is already registered"
                );
            }

            driver.setPhone(
                    dto.getPhone().trim()
            );
        }


        if (dto.getVehicleType() != null) {

            driver.setVehicleType(
                    dto.getVehicleType()
            );
        }
        if (dto.getCapacityKg() != null) {

            if (dto.getCapacityKg() <= 0) {

                throw new RuntimeException(
                        "Vehicle capacity must be greater than 0 kg"
                );
            }

            driver.setCapacityKg(
                    dto.getCapacityKg()
            );
        }


        if (dto.getPrice() != null) {

            if (dto.getPrice() < 0) {

                throw new RuntimeException(
                        "Price cannot be negative"
                );
            }

            driver.setPrice(
                    dto.getPrice()
            );
        }



        if (dto.getCurrentLocation() != null &&
                !dto.getCurrentLocation().isBlank()) {

            driver.setCurrentLocation(
                    dto.getCurrentLocation().trim()
            );
        }


        if (dto.getLatitude() != null) {

            if (dto.getLatitude() < -90 ||
                    dto.getLatitude() > 90) {

                throw new RuntimeException(
                        "Invalid latitude"
                );
            }

            driver.setLatitude(
                    dto.getLatitude()
            );
        }


        if (dto.getLongitude() != null) {

            if (dto.getLongitude() < -180 ||
                    dto.getLongitude() > 180) {

                throw new RuntimeException(
                        "Invalid longitude"
                );
            }

            driver.setLongitude(
                    dto.getLongitude()
            );
        }


        Driver savedDriver =
                driverRepository.save(driver);

        return convertToProfileDTO(
                savedDriver
        );
    }



    private DriverProfileDTO convertToProfileDTO(
            Driver driver
    ) {

        DriverProfileDTO response =
                new DriverProfileDTO();

        response.setId(
                driver.getId()
        );

        response.setName(
                driver.getName()
        );

        response.setPhone(
                driver.getPhone()
        );

        response.setVehicleType(
                driver.getVehicleType()
        );

        response.setCapacityKg(
                driver.getCapacityKg()
        );

        response.setPrice(
                driver.getPrice()
        );

        response.setRating(
                driver.getRating()
        );

        response.setCompletedRides(
                driver.getCompletedRides()
        );

        response.setAvailable(
                driver.getAvailable()
        );

        response.setCurrentLocation(
                driver.getCurrentLocation()
        );

        response.setLatitude(
                driver.getLatitude()
        );

        response.setLongitude(
                driver.getLongitude()
        );

        return response;
    }
}


