package com.busbooking.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.busbooking.dto.BookingRequestDTO;
import com.busbooking.dto.BookingResponseDTO;
import com.busbooking.entities.Booking;
import com.busbooking.entities.BookingSeat;
import com.busbooking.entities.Seat;
import com.busbooking.entities.Trip;
import com.busbooking.entities.User;
import com.busbooking.exceptions.BookingConflictException;
import com.busbooking.exceptions.InvalidSeatException;
import com.busbooking.exceptions.ResourceNotFoundException;
import com.busbooking.repositories.BookingRepository;
import com.busbooking.repositories.BookingSeatRepository;
import com.busbooking.repositories.SeatRepository;
import com.busbooking.repositories.TripRepository;
import com.busbooking.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatRepository seatRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public BookingService(
            BookingRepository bookingRepository,
            BookingSeatRepository bookingSeatRepository,
            SeatRepository seatRepository,
            TripRepository tripRepository,
            UserRepository userRepository) {

        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.seatRepository = seatRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("Authenticated user not found");
        }
        return user;
    }

    private void checkOwnership(Booking booking, User currentUser, boolean admin) {
        if (!admin && !booking.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not allowed to access this booking");
        }
    }

    private BookingResponseDTO toResponseDTO(Booking booking) {
        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(booking.getId());
        List<Long> seatIds = new ArrayList<>();
        for (BookingSeat bookingSeat : bookingSeats) {
            seatIds.add(bookingSeat.getSeat().getId());
        }
        return new BookingResponseDTO(
                booking.getId(),
                booking.getUser().getId(),
                booking.getTrip().getId(),
                seatIds
        );
    }

    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO dto, String email, boolean admin) {
        if (dto == null || dto.getTripId() == null) {
            throw new IllegalArgumentException("Trip ID is required");
        }
        if (dto.getSeatIds() == null || dto.getSeatIds().isEmpty()) {
            throw new InvalidSeatException("Select at least one seat");
        }
        if (dto.getSeatIds().stream().anyMatch(id -> id == null || id <= 0)) {
            throw new InvalidSeatException("Seat IDs must be positive numbers");
        }
        if (dto.getSeatIds().stream().distinct().count() != dto.getSeatIds().size()) {
            throw new InvalidSeatException("The same seat cannot be selected more than once");
        }

        User user = getAuthenticatedUser(email);
        Trip trip = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + dto.getTripId()));

        List<Seat> selectedSeats = new ArrayList<>();

        for (Long seatId : dto.getSeatIds()) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found with id: " + seatId));

            if (!seat.getBus().getId().equals(trip.getBus().getId())) {
                throw new InvalidSeatException("Seat " + seatId + " does not belong to the trip's bus");
            }

            List<BookingSeat> existingBookings = bookingSeatRepository.findBySeatAndTrip(seatId, trip.getId());
            if (!existingBookings.isEmpty()) {
                throw new BookingConflictException("Seat " + seatId + " is already booked for this trip");
            }

            selectedSeats.add(seat);
        }

        Booking booking = new Booking(user, trip);
        Booking savedBooking = bookingRepository.save(booking);

        List<Long> bookedSeatIds = new ArrayList<>();
        for (Seat seat : selectedSeats) {
            BookingSeat bookingSeat = new BookingSeat(savedBooking, seat);
            bookingSeatRepository.save(bookingSeat);
            bookedSeatIds.add(seat.getId());
        }

        return new BookingResponseDTO(
                savedBooking.getId(),
                user.getId(),
                trip.getId(),
                bookedSeatIds
        );
    }

    @Transactional
    public BookingResponseDTO findBookingById(Long id, String email, boolean admin) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        User currentUser = getAuthenticatedUser(email);
        checkOwnership(booking, currentUser, admin);
        return toResponseDTO(booking);
    }

    @Transactional
    public List<BookingResponseDTO> findBookingByUserId(Long userId, String email, boolean admin) {
        User currentUser = getAuthenticatedUser(email);
        if (!admin && !currentUser.getId().equals(userId)) {
            throw new AccessDeniedException("You can only view your own bookings");
        }
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        List<BookingResponseDTO> responses = new ArrayList<>();
        for (Booking booking : bookings) {
            responses.add(toResponseDTO(booking));
        }
        return responses;
    }

    @Transactional
    public String cancelBooking(Long bookingId, String email, boolean admin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        User currentUser = getAuthenticatedUser(email);
        checkOwnership(booking, currentUser, admin);
        List<BookingSeat> bookingSeats = bookingSeatRepository.findByBookingId(bookingId);
        bookingSeatRepository.deleteAll(bookingSeats);
        bookingRepository.delete(booking);
        return "Booking cancelled successfully";
    }
}
