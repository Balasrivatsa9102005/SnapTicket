package com.busbooking.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.ApiResponseDTO;
import com.busbooking.dto.RouteRequestDTO;
import com.busbooking.dto.RouteResponseDTO;
import com.busbooking.services.RouteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createRoute")
    public ApiResponseDTO createRoute(
            @Valid @RequestBody RouteRequestDTO dto) {
        return routeService.createRoute(dto);
    }

    @GetMapping("/findRoute/{id}")
    public RouteResponseDTO findRoute(@PathVariable Long id) {
        return routeService.findRoute(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateRoute/{id}")
    public ApiResponseDTO updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody RouteRequestDTO dto) {
        return routeService.updateRoute(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteRoute/{id}")
    public ApiResponseDTO deleteRoute(@PathVariable Long id) {
        return routeService.deleteRoute(id);
    }
}