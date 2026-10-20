package com.busbooking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record TripResponseDTO(
        Long id,
        Long busId,
        String busNumber,
        String operatorName,
        String busType,
        Long routeId,
        String source,
        String destination,
        int distance,
        LocalDate travelDate,
        LocalTime departureTime,
        LocalTime arrivalTime
) {}