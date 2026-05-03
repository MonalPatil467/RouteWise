package RouteWise.Transportation.controller;

import RouteWise.Transportation.dtos.AuthResponseDTO;
import RouteWise.Transportation.dtos.LoginRequestDTO;
import RouteWise.Transportation.dtos.UserSignupDTO;
import RouteWise.Transportation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // USER SIGNUP
    @PostMapping("/signup")
    public AuthResponseDTO signup(
            @RequestBody UserSignupDTO dto
    ) {

        return userService.signup(dto);
    }

    // USER LOGIN
    @PostMapping("/login")
    public AuthResponseDTO login(
            @RequestBody LoginRequestDTO dto
    ) {

        return userService.login(dto);
    }
}
