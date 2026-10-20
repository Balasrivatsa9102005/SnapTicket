package com.busbooking.dto;

import com.busbooking.enums.BusType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BusRequestDTO(
        @NotBlank String busNumber,
        @NotBlank String operatorName,
        @NotNull BusType busType,
        @Min(1) int totalSeats
) {}