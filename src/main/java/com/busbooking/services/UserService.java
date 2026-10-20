package com.busbooking.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busbooking.dto.ApiResponseDTO;
import com.busbooking.dto.AuthResponseDTO;
import com.busbooking.dto.LoginRequestDTO;
import com.busbooking.dto.RegisterRequestDTO;
import com.busbooking.dto.UserResponseDTO;
import com.busbooking.dto.UserUpdateRequestDTO;
import com.busbooking.entities.User;
import com.busbooking.exceptions.ResourceNotFoundException;
import com.busbooking.repositories.UserRepository;
import com.busbooking.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    private UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public ApiResponseDTO registerUser(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return new ApiResponseDTO(
                    false, "User already exists. Please log in.");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());

        user.setRole("USER");

        userRepository.save(user);

        return new ApiResponseDTO(true, "Registration successful");
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null ||
                !passwordEncoder.matches(
                        dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(
                    "Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return new AuthResponseDTO(token, "Bearer");
    }

    public UserResponseDTO findUserById(
            Long id, String email, boolean admin) {

        User currentUser = getAuthenticatedUser(email);

        if (!admin && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException(
                    "You can only view your own profile");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + id));

        return toDTO(user);
    }

    @Transactional
    public ApiResponseDTO updateById(
            Long id,
            UserUpdateRequestDTO dto,
            String email,
            boolean admin) {

        User currentUser = getAuthenticatedUser(email);

        if (!admin && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException(
                    "You can only update your own profile");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + id));

        User existingEmailUser =
                userRepository.findByEmail(dto.email());

        if (existingEmailUser != null
                && !existingEmailUser.getId().equals(id)) {
            return new ApiResponseDTO(
                    false, "Email address is already in use");
        }

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());

        userRepository.save(user);

        return new ApiResponseDTO(true, "Profile updated successfully");
    }

    @Transactional
    public ApiResponseDTO deleteById(
            Long id, String email, boolean admin) {

        User currentUser = getAuthenticatedUser(email);

        if (!admin && !currentUser.getId().equals(id)) {
            throw new AccessDeniedException(
                    "You can only delete your own account");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found: " + id));

        userRepository.delete(user);

        return new ApiResponseDTO(true, "Account deleted successfully");
    }

    private User getAuthenticatedUser(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "Authenticated user not found");
        }

        return user;
    }
}