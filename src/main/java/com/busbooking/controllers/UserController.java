package com.busbooking.controllers;

import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.ApiResponseDTO;
import com.busbooking.dto.AuthResponseDTO;
import com.busbooking.dto.LoginRequestDTO;
import com.busbooking.dto.RegisterRequestDTO;
import com.busbooking.dto.UserResponseDTO;
import com.busbooking.dto.UserUpdateRequestDTO;
import com.busbooking.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private boolean isAdmin(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return roles != null && roles.contains("ROLE_ADMIN");
    }

    @PostMapping("/register")
    public ApiResponseDTO registerUser(
            @Valid @RequestBody RegisterRequestDTO dto) {
        return userService.registerUser(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(
            @Valid @RequestBody LoginRequestDTO dto) {
        return userService.login(dto);
    }

    @GetMapping("/findUser/{id}")
    public UserResponseDTO findUser(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        return userService.findUserById(
                id, jwt.getSubject(), isAdmin(jwt));
    }

    @PutMapping("/updateUser/{id}")
    public ApiResponseDTO updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO dto,
            @AuthenticationPrincipal Jwt jwt) {
        return userService.updateById(
                id, dto, jwt.getSubject(), isAdmin(jwt));
    }

    @DeleteMapping("/deleteUser/{id}")
    public ApiResponseDTO deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        return userService.deleteById(
                id, jwt.getSubject(), isAdmin(jwt));
    }
}