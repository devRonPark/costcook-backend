package com.costcook.domain.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeDetailResponse {

    private Long recipeId;
    private String title;
    private String description;
    private String thumbnailUrl;
    private int rcpSno;
    private int servings;
    private int reviewCount;
    private int price;
    private double avgRatings;
    private List<IngredientResponse> ingredients;
	private boolean isFavorite;
	
    public static RecipeDetailResponse toDTO(RecipeResponse recipeResponse, List<IngredientResponse> ingredients) {
    	return RecipeDetailResponse.builder()
    		.recipeId(recipeResponse.getId())
    		.title(recipeResponse.getTitle())
    		.description(recipeResponse.getDescription())
    		.thumbnailUrl(recipeResponse.getThumbnailUrl())
    		.rcpSno(recipeResponse.getRcpSno())
    		.servings(recipeResponse.getServings())
    		.price(recipeResponse.getPrice())
    		.reviewCount(recipeResponse.getReviewCount())
    		.avgRatings(recipeResponse.getAvgRatings())
    		.ingredients(ingredients)
			.isFavorite(recipeResponse.isFavorite())
    		.build();
	}
}
