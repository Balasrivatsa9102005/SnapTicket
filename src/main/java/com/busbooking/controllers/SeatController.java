package com.busbooking.controllers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.SeatResponseDTO;
import com.busbooking.entities.Seat;
import com.busbooking.entities.Trip;
import com.busbooking.exceptions.ResourceNotFoundException;
import com.busbooking.repositories.BookingSeatRepository;
import com.busbooking.repositories.SeatRepository;
import com.busbooking.repositories.TripRepository;

@RestController
@RequestMapping("/api")
public class SeatController {

    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final TripRepository tripRepository;

    public SeatController(
            SeatRepository seatRepository,
            BookingSeatRepository bookingSeatRepository,
            TripRepository tripRepository) {
        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.tripRepository = tripRepository;
    }

    @GetMapping("/trips/{tripId}/seats")
    public List<SeatResponseDTO> getSeatsForTrip(@PathVariable Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + tripId));

        List<Seat> seats = seatRepository.findByBusId(trip.getBus().getId());

        Set<Long> bookedSeatIds = bookingSeatRepository.findBookedSeatIdsByTripId(tripId);

        return seats.stream()
                .map(s -> new SeatResponseDTO(
                        s.getId(),
                        s.getSeatNumber(),
                        bookedSeatIds.contains(s.getId())
                ))
                .collect(Collectors.toList());
    }
}