package com.costcook.domain.response;

import java.time.format.DateTimeFormatter;

import com.costcook.entity.Category;
import com.costcook.entity.RecipeItem;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeResponse {

	private Long id;
	private int rcpSno;
	private String title, description, thumbnailUrl, createdAt, updatedAt;
	private int servings, price, viewCount, bookmarkCount, commentCount;
	private double avgRatings;
	private Category category;
	
	// Recipe -> response 변환
	public static RecipeResponse toDTO(RecipeItem recipe) {
		return RecipeResponse.builder()
				.id(recipe.getId())
				.category(recipe.getCategory())
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
				.thumbnailUrl(recipe.getThumbnailUrl())
				.build();
	}
	
}
