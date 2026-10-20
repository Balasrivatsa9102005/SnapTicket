package com.busbooking.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.ApiResponseDTO;
import com.busbooking.dto.BusRequestDTO;
import com.busbooking.dto.BusResponseDTO;
import com.busbooking.services.BusService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createBus")
    public ApiResponseDTO createBus(
            @Valid @RequestBody BusRequestDTO dto) {
        return busService.createBus(dto);
    }

    @GetMapping("/findBus/{id}")
    public BusResponseDTO findBus(@PathVariable Long id) {
        return busService.findBus(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateBus/{id}")
    public ApiResponseDTO updateBus(
            @PathVariable Long id,
            @Valid @RequestBody BusRequestDTO dto) {
        return busService.updateBus(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteBus/{id}")
    public ApiResponseDTO deleteBus(@PathVariable Long id) {
        return busService.deleteBus(id);
    }
}