package com.busbooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busbooking.entities.Route;

public interface RouteRepository extends JpaRepository<Route,Long>{
	boolean existsBySourceAndDestination(String source,String destination);
	boolean existsById(Long id);
}
