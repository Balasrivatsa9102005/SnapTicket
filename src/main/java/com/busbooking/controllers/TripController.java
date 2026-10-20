package com.busbooking.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.ApiResponseDTO;
import com.busbooking.dto.TripRequestDTO;
import com.busbooking.dto.TripResponseDTO;
import com.busbooking.services.TripService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createTrip")
    public ApiResponseDTO createTrip(
            @Valid @RequestBody TripRequestDTO dto) {
        return tripService.createTrip(dto);
    }

    @GetMapping("/findTrip/{id}")
    public TripResponseDTO findTripById(@PathVariable Long id) {
        return tripService.findTripById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateTrip/{id}")
    public ApiResponseDTO updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody TripRequestDTO dto) {
        return tripService.updateTrip(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteTrip/{id}")
    public ApiResponseDTO deleteTrip(@PathVariable Long id) {
        return tripService.deleteTrip(id);
    }
}