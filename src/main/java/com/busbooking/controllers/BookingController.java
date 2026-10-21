package com.busbooking.controllers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.busbooking.dto.BookingRequestDTO;
import com.busbooking.dto.BookingResponseDTO;
import com.busbooking.services.BookingService;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/createBooking")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @RequestBody BookingRequestDTO dto,
            Authentication authentication) {

        if (authentication == null) {
            throw new org.springframework.security.authentication.BadCredentialsException(
                    "Missing or invalid token authentication context");
        }

        String email = authentication.getName(); 
        
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
                
        boolean isAdmin = roles.contains("ROLE_ADMIN");

        BookingResponseDTO response = bookingService.createBooking(dto, email, isAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        BookingResponseDTO response = bookingService.findBookingById(id, email, isAdmin);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/booking/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(
            @PathVariable Long userId,
            Authentication authentication) {

        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<BookingResponseDTO> responses = bookingService.findBookingByUserId(userId, email, isAdmin);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/booking/{id}")
    public ResponseEntity<String> cancelBooking(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        String message = bookingService.cancelBooking(id, email, isAdmin);
        return ResponseEntity.ok(message);
    }
}
