package com.busbooking.dto;

import java.util.List;

public class BookingResponseDTO {

    private Long bookingId;
    private Long userId;
    private Long tripId;
    private List<Long> seatIds;

    public BookingResponseDTO() {}

    public BookingResponseDTO(Long bookingId, Long userId, Long tripId, List<Long> seatIds) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.tripId = tripId;
        this.seatIds = seatIds;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public List<Long> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Long> seatIds) {
        this.seatIds = seatIds;
    }
}