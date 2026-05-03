package RouteWise.Transportation.service.Impl;

import RouteWise.Transportation.Entities.User;
import RouteWise.Transportation.Repositories.UserRepository;
import RouteWise.Transportation.dtos.AuthResponseDTO;
import RouteWise.Transportation.dtos.LoginRequestDTO;
import RouteWise.Transportation.dtos.UserSignupDTO;
import RouteWise.Transportation.mappers.UserMapper;
import RouteWise.Transportation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Override
    public AuthResponseDTO signup(UserSignupDTO dto) {

        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new RuntimeException("User already exists");
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

        boolean isPasswordCorrect =
                passwordEncoder.matches(
                        dto.getPassword(),
                        user.getPassword()
                );

        if (!isPasswordCorrect) {
            throw new RuntimeException("Invalid Password");
        }

        AuthResponseDTO response = new AuthResponseDTO();

        response.setId(user.getId());

        response.setRole("USER");

        response.setMessage("Login Successful");

        return response;
    }
}
