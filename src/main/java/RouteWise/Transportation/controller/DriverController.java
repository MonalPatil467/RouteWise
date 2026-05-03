package RouteWise.Transportation.controller;

import RouteWise.Transportation.dtos.AuthResponseDTO;
import RouteWise.Transportation.dtos.DriverResponseDTO;
import RouteWise.Transportation.dtos.DriverSignupDTO;
import RouteWise.Transportation.dtos.LoginRequestDTO;
import RouteWise.Transportation.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/driver")
public class DriverController {
    private final DriverService driverService;

    // DRIVER SIGNUP
    @PostMapping("/signup")
    public AuthResponseDTO signup(
            @RequestBody DriverSignupDTO dto
    ) {

        return driverService.signup(dto);
    }

    // DRIVER LOGIN
    @PostMapping("/login")
    public AuthResponseDTO login(
            @RequestBody LoginRequestDTO dto
    ) {

        return driverService.login(dto);
    }
}
