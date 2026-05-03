package RouteWise.Transportation.service.Impl;

import RouteWise.Transportation.Entities.Driver;
import RouteWise.Transportation.Repositories.DriverRepository;
import RouteWise.Transportation.dtos.AuthResponseDTO;
import RouteWise.Transportation.dtos.DriverResponseDTO;
import RouteWise.Transportation.dtos.DriverSignupDTO;
import RouteWise.Transportation.dtos.LoginRequestDTO;
import RouteWise.Transportation.mappers.DriverMapper;
import RouteWise.Transportation.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;

    private final PasswordEncoder passwordEncoder;

    private final DriverMapper driverMapper;

    @Override
    public AuthResponseDTO signup(DriverSignupDTO dto) {

        if (driverRepository.existsByPhone(dto.getPhone())) {
            throw new RuntimeException("Driver already exists");
        }

        Driver driver = driverMapper.toEntity(dto);

        driver.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        Driver savedDriver = driverRepository.save(driver);

        AuthResponseDTO response = new AuthResponseDTO();

        response.setId(savedDriver.getId());

        response.setRole("DRIVER");

        response.setMessage("Driver Signup Successful");

        return response;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {

        Driver driver = driverRepository
                .findByPhone(dto.getPhone())
                .orElseThrow(() ->
                        new RuntimeException("Driver not found"));

        boolean isPasswordCorrect =
                passwordEncoder.matches(
                        dto.getPassword(),
                        driver.getPassword()
                );

        if (!isPasswordCorrect) {
            throw new RuntimeException("Invalid Password");
        }

        AuthResponseDTO response = new AuthResponseDTO();

        response.setId(driver.getId());

        response.setRole("DRIVER");

        response.setMessage("Driver Login Successful");

        return response;
    }

    @Override
    public List<DriverResponseDTO> getAvailableDrivers(
            String pickupLocation
    ) {

        List<Driver> drivers =
                driverRepository
                        .findByAvailableTrueAndCurrentLocation(
                                pickupLocation
                        );

        return drivers
                .stream()
                .map(driverMapper::toDTO)
                .collect(Collectors.toList());
    }
}
