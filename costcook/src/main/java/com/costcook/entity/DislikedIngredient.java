package com.costcook.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "disliked_ingredients")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DislikedIngredient {

    @EmbeddedId
    private DislikedIngredientId id; // 복합키

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", updatable = false) // user_id는 변경 불가
    private User user;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", updatable = false) // category_id는 변경 불가
    private Category category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Soft delete 메서드
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
