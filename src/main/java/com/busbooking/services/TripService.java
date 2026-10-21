package com.busbooking.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.busbooking.dto.ApiResponseDTO;
import com.busbooking.dto.TripRequestDTO;
import com.busbooking.dto.TripResponseDTO;
import com.busbooking.entities.Bus;
import com.busbooking.entities.Route;
import com.busbooking.entities.Trip;
import com.busbooking.exceptions.ResourceNotFoundException;
import com.busbooking.repositories.BusRepository;
import com.busbooking.repositories.RouteRepository;
import com.busbooking.repositories.TripRepository;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;

    public TripService(
            TripRepository tripRepository,
            BusRepository busRepository,
            RouteRepository routeRepository) {
        this.tripRepository = tripRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
    }

    private TripResponseDTO toDTO(Trip trip) {
        Bus bus = trip.getBus();
        Route route = trip.getRoute();

        return new TripResponseDTO(
                trip.getId(),
                bus.getId(),
                bus.getBusNumber(),
                bus.getOperatorName(),
                bus.getBusType().name(),
                route.getId(),
                route.getSource(),
                route.getDestination(),
                route.getDistance(),
                trip.getTravelDate(),
                trip.getDepartureTime(),
                trip.getArrivalTime()
        );
    }

    private void validateTimes(TripRequestDTO dto) {
        if (dto.departureTime() == null || dto.arrivalTime() == null) {
            throw new IllegalArgumentException(
                    "Departure time and arrival time are required");
        }

        if (dto.arrivalTime().equals(dto.departureTime())) {
            throw new IllegalArgumentException(
                    "Arrival time cannot be the same as departure time");
        }
    }

    @Transactional
    public ApiResponseDTO createTrip(TripRequestDTO dto) {
        validateTimes(dto);

        Bus bus = busRepository.findById(dto.busId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bus not found: " + dto.busId()));

        Route route = routeRepository.findById(dto.routeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Route not found: " + dto.routeId()));

        List<Trip> overlapping =
                tripRepository.findOverlappingTrips(
                        bus.getId(),
                        dto.travelDate(),
                        dto.departureTime(),
                        dto.arrivalTime()
                );

        if (!overlapping.isEmpty()) {
            return new ApiResponseDTO(
                    false, "Bus already has an overlapping trip");
        }

        Trip trip = new Trip(
                bus,
                route,
                dto.travelDate(),
                dto.departureTime(),
                dto.arrivalTime()
        );

        tripRepository.save(trip);

        return new ApiResponseDTO(true, "Trip created successfully");
    }

    public TripResponseDTO findTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Trip not found: " + id));

        return toDTO(trip);
    }

    @Transactional
    public ApiResponseDTO updateTrip(Long id, TripRequestDTO dto) {
        validateTimes(dto);

        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Trip not found: " + id));

        Bus bus = busRepository.findById(dto.busId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bus not found: " + dto.busId()));

        Route route = routeRepository.findById(dto.routeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Route not found: " + dto.routeId()));

        List<Trip> overlapping =
                tripRepository.findOverlappingTripsForUpdate(
                        bus.getId(),
                        dto.travelDate(),
                        dto.departureTime(),
                        dto.arrivalTime(),
                        id
                );

        if (!overlapping.isEmpty()) {
            return new ApiResponseDTO(
                    false, "Bus already has an overlapping trip");
        }

        trip.setBus(bus);
        trip.setRoute(route);
        trip.setTravelDate(dto.travelDate());
        trip.setDepartureTime(dto.departureTime());
        trip.setArrivalTime(dto.arrivalTime());

        tripRepository.save(trip);

        return new ApiResponseDTO(true, "Trip updated successfully");
    }

    public ApiResponseDTO deleteTrip(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Trip not found: " + id));

        tripRepository.delete(trip);

        return new ApiResponseDTO(true, "Trip deleted successfully");
    }
}