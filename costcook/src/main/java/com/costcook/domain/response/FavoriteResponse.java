package com.costcook.domain.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteResponse {

	private Long recipeId;
	private String recipeTitle;
	private String recipeThumbNail;
	private Double recipeAvgRatings;
	private int recipePrice;
	
	// favorite -> response 변환
	public static FavoriteResponse toDTO(RecipeResponse recipeResponse) {
		return FavoriteResponse.builder()
				.recipeId(recipeResponse.getId())
				.recipeTitle(recipeResponse.getTitle())
				.recipeThumbNail(recipeResponse.getThumbnailUrl())
				.recipeAvgRatings(recipeResponse.getAvgRatings())
				.recipePrice(recipeResponse.getPrice())
				.build();
	}
	
}
