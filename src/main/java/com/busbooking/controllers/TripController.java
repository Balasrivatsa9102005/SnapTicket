package com.busbooking.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    

    @GetMapping("/trips/search")
    public List<TripResponseDTO> searchTrips(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(required = false) String date) {
        return tripService.searchTrips(source, destination, date);
    }
}