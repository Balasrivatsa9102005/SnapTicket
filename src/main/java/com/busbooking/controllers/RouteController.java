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
import org.springframework.web.bind.annotation.RestController;

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
    
    @GetMapping("/routes/sources")
    public List<String> allSources() {
        return routeService.findAllSources();
    }

    @GetMapping("/routes/destinations")
    public List<String> allDestinations() {
        return routeService.findAllDestinations();
    }
}