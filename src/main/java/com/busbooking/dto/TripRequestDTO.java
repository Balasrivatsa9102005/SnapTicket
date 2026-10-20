package com.busbooking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record TripRequestDTO(
        @NotNull Long busId,
        @NotNull Long routeId,
        @NotNull LocalDate travelDate,
        @NotNull LocalTime departureTime,
        @NotNull LocalTime arrivalTime
) {}