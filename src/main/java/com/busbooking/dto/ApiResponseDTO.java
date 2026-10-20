package com.busbooking.dto;

public record ApiResponseDTO(
        boolean success,
        String message
) {}