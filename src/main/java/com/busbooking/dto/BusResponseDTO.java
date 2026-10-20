package com.busbooking.dto;

import com.busbooking.enums.BusType;

public record BusResponseDTO(
        Long id,
        String busNumber,
        String operatorName,
        BusType busType,
        int totalSeats
) {}