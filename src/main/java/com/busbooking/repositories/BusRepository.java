package com.busbooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busbooking.entities.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {
	boolean existsByBusNumber(String busNumber);
}
