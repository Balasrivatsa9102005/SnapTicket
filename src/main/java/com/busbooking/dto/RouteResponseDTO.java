package com.busbooking.dto;

public record RouteResponseDTO(
        Long id,
        String source,
        String destination,
        int distance
) {}