package com.busbooking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RouteRequestDTO(
        @NotBlank String source,
        @NotBlank String destination,
        @Min(1) int distance
) {}