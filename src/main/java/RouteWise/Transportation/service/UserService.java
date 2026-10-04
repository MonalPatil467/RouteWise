package RouteWise.Transportation.service;

import RouteWise.Transportation.dtos.*;


public interface UserService {
    AuthResponseDTO signup(UserSignupDTO dto);

    AuthResponseDTO login(LoginRequestDTO dto);

    UserProfileDTO getProfile(Long userId);

    UserProfileDTO updateProfile(Long userId, UserProfileDTO dto);

    AuthResponseDTO changePassword(
            Long userId,
            String oldPassword,
            String newPassword
    );
}
