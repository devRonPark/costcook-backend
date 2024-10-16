package com.costcook.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.costcook.domain.PlatformTypeEnum;

@Entity
@Table(name = "social_accounts")
@Data
@NoArgsConstructor
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 식별자

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 (외래 키)

    @Enumerated(EnumType.STRING)
    @Column(name = "platform_type")
    private PlatformTypeEnum platformType; // 플랫폼 타입 (KAKAO, GOOGLE)

    @Column(name = "social_key", length = 255)
    private String socialKey; // 소셜 키

    @CreatedDate
    @Column(name = "connected_at", nullable = false)
    private LocalDateTime connectedAt; // 연결된 시간
}
