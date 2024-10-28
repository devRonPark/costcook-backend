package com.costcook.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reviews")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "user_id", updatable = false) // user_id는 변경 불가
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", updatable = false) // category_id는 변경 불가
    private Recipe recipe;
	
	// 평점
	@Column(nullable = false)
	private int score;
	
	// 리뷰 내용
	@Column(nullable = false)
	private String comment;
	
	// 상태값 (공개 : false, 비공개 : true)
	@Builder.Default
	@Column(nullable = false)
	private boolean status = false;
	
	// 등록일
	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	// 수정일
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	// 삭제일
	@Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
