package com.busbooking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.busbooking.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
