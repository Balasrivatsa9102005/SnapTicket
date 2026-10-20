package com.busbooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busbooking.entities.Seat;

public interface SeatRepository extends JpaRepository<Seat,Long>{

}
