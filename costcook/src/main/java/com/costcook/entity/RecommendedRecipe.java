package com.costcook.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "recommended_recipes")
public class RecommendedRecipe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 추천 레시피 ID

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt; // 생성 날짜

    @Column(name = "is_used")
    private boolean isUsed; // 사용 여부

    @Column(name = "week_number")
    private int weekNumber; // 주 번호 (1~52)

    @Column(name = "year")
    private int year; // 연도

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private Recipe recipe;  // Recipe 엔티티와의 관계

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;  // User 엔티티와의 관계
}

