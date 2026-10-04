package RouteWise.Transportation.service.Impl;


import RouteWise.Transportation.Entities.User;
import RouteWise.Transportation.Repositories.UserRepository;
import RouteWise.Transportation.dtos.AuthResponseDTO;
import RouteWise.Transportation.dtos.LoginRequestDTO;
import RouteWise.Transportation.dtos.UserProfileDTO;
import RouteWise.Transportation.dtos.UserSignupDTO;
import RouteWise.Transportation.mappers.UserMapper;
import RouteWise.Transportation.security.JwtService;
import RouteWise.Transportation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public AuthResponseDTO signup(UserSignupDTO dto) {

        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new RuntimeException("User already exists with this phone number");
        }

        User user = userMapper.toEntity(dto);

        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );

        User savedUser = userRepository.save(user);

        AuthResponseDTO response = new AuthResponseDTO();

        response.setId(savedUser.getId());
        response.setRole("USER");
        response.setMessage("User Signup Successful");

        return response;
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {

        User user = userRepository
                .findByPhone(dto.getPhone())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Invalid Password"
            );
        }

        String token = jwtService.generateToken(
                user.getPhone(),
                user.getId(),
                "USER"
        );

        AuthResponseDTO response =
                new AuthResponseDTO();

        response.setId(user.getId());
        response.setRole("USER");
        response.setMessage("Login Successful");
        response.setToken(token);

        return response;
    }
    @Override
    public UserProfileDTO getProfile(Long userId) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        UserProfileDTO response = new UserProfileDTO();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setPhone(user.getPhone());

        return response;
    }

    @Override
    public UserProfileDTO updateProfile(
            Long userId,
            UserProfileDTO dto
    ) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (dto.getPhone() != null &&
                !dto.getPhone().equals(user.getPhone()) &&
                userRepository.existsByPhone(dto.getPhone())) {

            throw new RuntimeException(
                    "Phone number already registered"
            );
        }

        if (dto.getName() != null &&
                !dto.getName().isBlank()) {

            user.setName(dto.getName());
        }

        if (dto.getPhone() != null &&
                !dto.getPhone().isBlank()) {

            user.setPhone(dto.getPhone());
        }

        User updatedUser =
                userRepository.save(user);

        UserProfileDTO response = new UserProfileDTO();

        response.setId(updatedUser.getId());
        response.setName(updatedUser.getName());
        response.setPhone(updatedUser.getPhone());

        return response;
    }

    @Override
    public AuthResponseDTO changePassword(
            Long userId,
            String oldPassword,
            String newPassword
    ) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                oldPassword,
                user.getPassword()
        )) {
            throw new RuntimeException(
                    "Old password is incorrect"
            );
        }

        user.setPassword(
                passwordEncoder.encode(newPassword)
        );

        userRepository.save(user);

        AuthResponseDTO response =
                new AuthResponseDTO();

        response.setId(user.getId());
        response.setRole("USER");
        response.setMessage(
                "Password Changed Successfully"
        );

        return response;
    }
}