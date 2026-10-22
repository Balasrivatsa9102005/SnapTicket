package com.busbooking.dto;

public record SeatResponseDTO(
        Long id,
        String seatNumber,
        boolean booked
) {}