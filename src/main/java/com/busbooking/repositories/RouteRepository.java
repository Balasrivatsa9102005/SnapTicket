package com.busbooking.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.busbooking.entities.Route;

public interface RouteRepository extends JpaRepository<Route,Long>{
	boolean existsBySourceAndDestination(String source,String destination);
	boolean existsById(Long id);
	@Query("SELECT DISTINCT r.source FROM Route r ORDER BY r.source")
	List<String> findDistinctSources();

	@Query("SELECT DISTINCT r.destination FROM Route r ORDER BY r.destination")
	List<String> findDistinctDestinations();
}
