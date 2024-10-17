package com.costcook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.costcook.entity.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
	Optional<EmailVerification> findByUserEmail(String email);
	Optional<EmailVerification> findTopByUserEmailOrderByCreatedAtDesc(String userEmail);
}
