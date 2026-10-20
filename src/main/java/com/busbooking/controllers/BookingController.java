
package com.busbooking.controllers;

import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.busbooking.dto.BookingRequestDTO;
import com.busbooking.dto.BookingResponseDTO;
import com.busbooking.services.BookingService;

@RestController
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    private boolean isAdmin(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        return roles != null && roles.contains("ROLE_ADMIN");
    }

    @PostMapping("/api/createBooking")
    public BookingResponseDTO createBooking(
            @RequestBody BookingRequestDTO dto,
            @AuthenticationPrincipal Jwt jwt) {

        return bookingService.createBooking(
                dto, jwt.getSubject(), isAdmin(jwt));
    }

    @GetMapping("/api/findBooking/{id}")
    public BookingResponseDTO findBookingById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        return bookingService.findBookingById(
                id, jwt.getSubject(), isAdmin(jwt));
    }

    @GetMapping("/api/userBookings/{userId}")
    public List<BookingResponseDTO> findBookingByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal Jwt jwt) {

        return bookingService.findBookingByUserId(
                userId, jwt.getSubject(), isAdmin(jwt));
    }

    @DeleteMapping("/api/cancelBooking/{id}")
    public String cancelBooking(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        return bookingService.cancelBooking(
                id, jwt.getSubject(), isAdmin(jwt));
    }
}
