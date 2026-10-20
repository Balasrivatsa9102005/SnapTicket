package com.busbooking.dto;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String phone
) {}