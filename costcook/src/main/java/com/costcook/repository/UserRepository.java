package com.costcook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.costcook.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
