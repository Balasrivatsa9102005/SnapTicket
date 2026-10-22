package com.busbooking.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.busbooking.entities.BookingSeat;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
	
	@Query("""
			    SELECT bs
			    FROM BookingSeat bs
			    WHERE bs.seat.id = :seatId
			      AND bs.booking.trip.id = :tripId
			""")
	List<BookingSeat> findBySeatAndTrip(@Param("seatId") Long seatId, @Param("tripId") Long tripId);
	
	List<BookingSeat> findByBookingId(Long bookingId);
	
	@Query("SELECT bs.seat.id FROM BookingSeat bs WHERE bs.booking.trip.id = :tripId")
    Set<Long> findBookedSeatIdsByTripId(@Param("tripId") Long tripId);
	

}
