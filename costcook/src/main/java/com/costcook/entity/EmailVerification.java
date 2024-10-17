package com.costcook.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "email_verifications")
@EntityListeners(AuditingEntityListener.class) // 생성, 수정 날짜 추적 -> Application.java (@EnableJpaAuditing)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성
    private Long id;

    @Column(name = "user_email", nullable = false)
    private String userEmail; // 인증 코드를 받을 사용자 이메일

    @Column(name = "verification_code", nullable = false, length = 6)
    private String verificationCode; // 6자리 인증 코드

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 인증 코드 생성 시간

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt; // 인증 코드 만료 시간 (생성 후 5분)
    
    /**
     * 엔티티가 처음으로 저장될 때 실행되는 메서드
     * createdAt 필드는 현재 시간으로 설정되고,
     * expiresAt 필드는 createdAt + 5분으로 설정된다.
     */
    @PrePersist
    public void onCreate() {
    	this.createdAt = LocalDateTime.now();  // 현재 시간 설정
        this.expiresAt = this.createdAt.plusMinutes(5);  // 5분 후 만료 시간 설정
    }
}
