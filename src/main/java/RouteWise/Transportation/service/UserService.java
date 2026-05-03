package RouteWise.Transportation.service;

import RouteWise.Transportation.dtos.AuthResponseDTO;
import RouteWise.Transportation.dtos.LoginRequestDTO;
import RouteWise.Transportation.dtos.UserSignupDTO;

public interface UserService {
    AuthResponseDTO signup(UserSignupDTO dto);

    AuthResponseDTO login(LoginRequestDTO dto);

    //UserResponseDTO getUserById(Long id);
}
