package com.costcook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.costcook.entity.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
}
