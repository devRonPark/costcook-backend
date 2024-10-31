package com.costcook.service;

import com.costcook.domain.response.RecipeListResponse;
import com.costcook.domain.response.RecipeResponse;
import com.costcook.domain.response.WeeklyRecipesResponse;

public interface RecipeService {

	// 레시피 목록 가져오기
	RecipeListResponse getRecipes(int page, int size, String sort, String order);
	
	// 레시피 총 개수
	long getTotalRecipes();
	
	// 레시피 ID를 통해 레시피 가져오기 -> 상세보기
	RecipeResponse getRecipeById(Long id);

	RecipeListResponse searchRecipes(String keyword, int page);

	WeeklyRecipesResponse getRecipesByBudget(int budget);
}
