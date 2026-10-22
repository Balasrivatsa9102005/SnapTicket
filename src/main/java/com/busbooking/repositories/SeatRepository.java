package com.busbooking.repositories;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;


import com.busbooking.entities.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
	
    List<Seat> findByBusId(Long busId);
    
}