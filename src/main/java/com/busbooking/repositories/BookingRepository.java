package com.busbooking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busbooking.entities.Booking;

public interface BookingRepository extends JpaRepository<Booking,Long>{
	List<Booking> findByUserId(Long userId);
}
