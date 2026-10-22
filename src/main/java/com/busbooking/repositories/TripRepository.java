package com.busbooking.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.busbooking.entities.Trip;

public interface TripRepository extends JpaRepository<Trip,Long>{
	
	@Query("""
		    SELECT t
		    FROM Trip t
		    WHERE t.bus.id = :busId
		      AND t.travelDate = :travelDate
		      AND t.departureTime < :arrivalTime
		      AND t.arrivalTime > :departureTime
		""")
		List<Trip> findOverlappingTrips(
		        @Param("busId") Long busId,
		        @Param("travelDate") LocalDate travelDate,
		        @Param("departureTime") LocalTime departureTime,
		        @Param("arrivalTime") LocalTime arrivalTime
		);
	@Query("""
		    SELECT t
		    FROM Trip t
		    WHERE t.bus.id = :busId
		      AND t.travelDate = :travelDate
		      AND t.departureTime < :arrivalTime
		      AND t.arrivalTime > :departureTime
		      AND t.id != :tripId
		""")
		List<Trip> findOverlappingTripsForUpdate(
		        @Param("busId") Long busId,
		        @Param("travelDate") LocalDate travelDate,
		        @Param("departureTime") LocalTime departureTime,
		        @Param("arrivalTime") LocalTime arrivalTime,
		        @Param("tripId") Long tripId
		);
	
	@Query("""
	    SELECT t FROM Trip t
	    WHERE LOWER(t.route.source) = LOWER(:source)
	      AND LOWER(t.route.destination) = LOWER(:destination)
	      AND (:date IS NULL OR t.travelDate = :date)
	    ORDER BY t.departureTime
	""")
	List<Trip> searchTrips(
	    @Param("source") String source,
	    @Param("destination") String destination,
	    @Param("date") LocalDate date);
	
}
