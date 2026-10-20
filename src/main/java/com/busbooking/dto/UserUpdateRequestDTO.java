package com.busbooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequestDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String phone
) {}