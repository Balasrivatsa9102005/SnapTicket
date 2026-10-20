package com.busbooking.services;

import org.springframework.stereotype.Service;

import com.busbooking.dto.ApiResponseDTO;
import com.busbooking.dto.RouteRequestDTO;
import com.busbooking.dto.RouteResponseDTO;
import com.busbooking.entities.Route;
import com.busbooking.exceptions.ResourceNotFoundException;
import com.busbooking.repositories.RouteRepository;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    private RouteResponseDTO toDTO(Route route) {
        return new RouteResponseDTO(
                route.getId(),
                route.getSource(),
                route.getDestination(),
                route.getDistance()
        );
    }

    public ApiResponseDTO createRoute(RouteRequestDTO dto) {
        if (routeRepository.existsBySourceAndDestination(
                dto.source(), dto.destination())) {
            return new ApiResponseDTO(false, "Route already exists");
        }

        Route route = new Route(
                dto.source(),
                dto.destination(),
                dto.distance()
        );

        routeRepository.save(route);

        return new ApiResponseDTO(true, "Route created successfully");
    }

    public RouteResponseDTO findRoute(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Route not found: " + id));

        return toDTO(route);
    }

    public ApiResponseDTO updateRoute(Long id, RouteRequestDTO dto) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Route not found: " + id));

        boolean sameRoute =
                route.getSource().equalsIgnoreCase(dto.source())
                && route.getDestination()
                        .equalsIgnoreCase(dto.destination());

        if (!sameRoute && routeRepository.existsBySourceAndDestination(
                dto.source(), dto.destination())) {
            return new ApiResponseDTO(false, "Route already exists");
        }

        route.setSource(dto.source());
        route.setDestination(dto.destination());
        route.setDistance(dto.distance());

        routeRepository.save(route);

        return new ApiResponseDTO(true, "Route updated successfully");
    }

    public ApiResponseDTO deleteRoute(Long id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Route not found: " + id));

        routeRepository.delete(route);

        return new ApiResponseDTO(true, "Route deleted successfully");
    }
}