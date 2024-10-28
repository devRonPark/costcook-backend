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
    private int servings;
    private int viewCount;
    private int favoriteCount;
    private int commentCount;
    private int price;
    private double avgRatings;
    private List<IngredientResponse> ingredients;
	
    public static RecipeDetailResponse toDTO(RecipeResponse recipeResponse, List<IngredientResponse> ingredients) {
    	return RecipeDetailResponse.builder()
    		.recipeId(recipeResponse.getId())
    		.title(recipeResponse.getTitle())
    		.description(recipeResponse.getDescription())
    		.thumbnailUrl(recipeResponse.getThumbnailUrl())
    		.servings(recipeResponse.getServings())
    		.viewCount(recipeResponse.getViewCount())
    		.price(recipeResponse.getPrice())
    		// .favoriteCount(recipeResponse.getFavoriteCount())
    		.commentCount(recipeResponse.getCommentCount())
    		.avgRatings(recipeResponse.getAvgRatings())
    		.ingredients(ingredients)
    		.build();
	}
}
