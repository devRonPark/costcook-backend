package com.costcook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.costcook.domain.RoleEnum;
import com.costcook.domain.StatusEnum;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class) // 생성, 수정 날짜 추적 -> Application.java (@EnableJpaAuditing)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false) // 이 필드 값은 생성할 때만 값 설정 가능.
    private Long id;

    @Column(length = 255, unique = true) // 해당 컬럼이 유일해야 한다는 제약 조건을 설정
    private String email;

    @Column(length = 100, nullable = true) // 선택 입력 정보
    private String nickname;

    @Column(length = 255, nullable = true)  // 선택 입력 정보
    private String profileUrl;

    @Column(name = "service_agreement")
    private Boolean serviceAgreement;

    @Column(name = "privacy_policy_agreement")
    private Boolean privacyPolicyAgreement;

    @Column(name = "additional_info_agreement")
    private Boolean additionalInfoAgreement;

    @Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private RoleEnum role = RoleEnum.ROLE_USER; // 권한 컬럼 추가 (기본값 ROLE_USER)

    @Column(name = "warning_count")
    private Integer warningCount;

    @Column(name = "refresh_token", length = 255)
    private String refreshToken; // 리프레시 토큰

    @CreatedDate
    @Column(name = "created_at", updatable = false) // 이 필드 값은 생성할 때만 값 설정 가능.
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private StatusEnum status = StatusEnum.ACTIVE; // 기본값을 ACTIVE로 설정, TINYINT로 매핑됨

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }    

    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<SocialAccount> socialAccounts; // 소셜 계정 리스트
}
