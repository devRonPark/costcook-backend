package com.costcook.domain.response;

import java.time.format.DateTimeFormatter;

import com.costcook.entity.Category;
import com.costcook.entity.Recipe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeResponse {

	private Long id;
	private int rcpSno;
	private String title, description, thumbnailUrl, createdAt, updatedAt;
	private int servings, price, viewCount, favoriteCount, reviewCount;
	private double avgRatings;
	private Category category;

	// Recipe -> response 변환
	// 전체 목록
	public static RecipeResponse toDTO(Recipe recipe, double avgRatings, int reviewCount, Long totalPrice) {		
		// 평점 소수점 첫째자리까지 반올림
		double roundedAvgRatings = Math.round(avgRatings * 10) / 10.0;
		
		return RecipeResponse.builder()
				.id(recipe.getId())
				.category(recipe.getCategory())
				.rcpSno(recipe.getRcpSno())
				.title(recipe.getTitle())
				.description(recipe.getDescription())
        		.thumbnailUrl(recipe.getThumbnailUrl())
				.createdAt(recipe.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
				.updatedAt(recipe.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
				.servings(recipe.getServings())
				.price(totalPrice.intValue())
				.viewCount(recipe.getViewCount())
				.reviewCount(reviewCount)
				.avgRatings(roundedAvgRatings)
				.build();
	}
}
