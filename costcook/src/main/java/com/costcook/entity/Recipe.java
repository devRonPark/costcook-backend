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
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;
	
	// 카테고리 식별자 (디폴트 null, 외래키(카테고리 테이블))
	@Column(nullable = true)
	private Long categoryId;
	
	// 만개 레시피 제공 데이터 고유번호
	@Column
	private int rcpSno;
	
	// 레시피 이름
	@Column(nullable = false)
	private String title;
	
//	// 레시피 이미지
//	@JoinColumn(name = "image_id", nullable = true)
//	@ManyToOne
//	private ImageFile image;
	
	// 레시피 설명 (null 허용)
	@Column(nullable = true)
	private String description;
	
	// 몇인분 (디폴트 1)
	@Column
	private int servings;
	
	// 가격
	@Column
	private int price;
	
	// 등록일
	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	// 수정일
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	// 조회수 (디폴트 0)
	@Column
	private int viewCount;
	
	// 즐겨찾기 수 (디폴트 0)
	@Column
	private int bookmarkCount;
	
	// 댓글 수 (디폴트 0)
	@Column
	private int commentCount;
	
	// 평점 (디폴트 0.0)
	@Column
	private double avgRatings;
	
	

}
