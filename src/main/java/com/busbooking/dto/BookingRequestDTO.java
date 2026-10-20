
package com.busbooking.dto;

import java.util.List;

public class BookingRequestDTO {

    private Long tripId;
    private List<Long> seatIds;

    public BookingRequestDTO() {}

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
