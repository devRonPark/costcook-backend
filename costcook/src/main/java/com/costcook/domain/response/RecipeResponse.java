package com.costcook.domain.response;

import java.time.format.DateTimeFormatter;

import com.costcook.entity.Recipe;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeResponse {

	private Long id, categoryId;
	private int rcpSno;
	private String title, description, createdAt, updatedAt;
	private int servings, price, viewCount, bookmarkCount, commentCount;
	private double avgRatings;
	
//	private UserResponse author;
//	private FileDTO image;
	
	// Recipe -> response 변환
	public static RecipeResponse toDTO(Recipe recipe) {
		return RecipeResponse.builder()
				.id(recipe.getId())
				.categoryId(recipe.getCategoryId())
				.rcpSno(recipe.getRcpSno())
				.title(recipe.getTitle())
				.description(recipe.getDescription())
				.createdAt(recipe.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
				.createdAt(recipe.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
				.servings(recipe.getServings())
				.price(recipe.getPrice())
				.viewCount(recipe.getViewCount())
				.bookmarkCount(recipe.getBookmarkCount())
				.commentCount(recipe.getCommentCount())
				.avgRatings(recipe.getAvgRatings())
				.build();
	}
	
}
