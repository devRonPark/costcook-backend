package com.costcook.entity;

import java.time.LocalDateTime;
import java.util.List;

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
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
@Table(name = "recipes")
public class Recipe {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;
	
	// 카테고리 식별자 (디폴트 null, 외래키(카테고리 테이블))
	@ManyToOne
	@JoinColumn(name = "category_id", nullable = true)
	private Category category;
	
	// 만개 레시피 제공 데이터 고유번호
	@Column(name="rcp_sno", nullable = false)
	@Builder.Default()
	private int rcpSno = 0;
	
	// 레시피 이름
	@Column(nullable = false)
	private String title;
	
	// 레시피 이미지
	@Column(nullable = true)
	private String thumbnailUrl;
	
	// 레시피 설명 (null 허용)
	@Column(nullable = true)
	private String description;
	
	// 몇인분 (디폴트 1)
	@Column(nullable = false)
	@Builder.Default()
	private int servings = 1;
	
//	// 가격(1인분 기준)
//	@Column(nullable = false)
//	@Builder.Default()
//	private int price = 0;
	
	// 등록일
	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	// 수정일
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	// 조회수 (디폴트 0)
	@Column(nullable = false)
	@Builder.Default()
	private int viewCount = 0;
	
//	// 즐겨찾기 수 (디폴트 0)
//	@Column(nullable = false)
//	@Builder.Default()
//	private int favoriteCount = 0;
//	
//	// 댓글 수 (디폴트 0)
//	@Column(nullable = false)
//	@Builder.Default()
//	private int commentCount = 0;
//	
//	// 평점 (디폴트 0.0)
//	@Column(nullable = false)
//	@Builder.Default()
//	private double avgRatings = 0.0;
	
//	// 가상 컬럼 (DB에 저장되지 않음)
//	// 레시피 총 가격 계산
//	@Transient
//	public int getTotalPrice(List<RecipeIngredient> recipeIngredients) {
//		return recipeIngredients.stream()
//				.filter(recipeIngredient -> recipeIngredient.getRecipe() != null && recipeIngredient.getRecipe().getId().equals(this.id)) // 레시피 ID로 필터링
//				.mapToInt(RecipeIngredient::getPrice)
//				.sum();
//	}

}
