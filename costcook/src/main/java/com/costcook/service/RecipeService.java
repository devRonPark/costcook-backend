package com.costcook.service;

import java.util.List;

import com.costcook.domain.request.RecommendedRecipeRequest;
import com.costcook.domain.response.RecipeListResponse;
import com.costcook.domain.response.RecipeResponse;
import com.costcook.domain.response.WeeklyRecipesResponse;
import com.costcook.entity.User;

public interface RecipeService {

	// 레시피 목록 가져오기
	RecipeListResponse getRecipes(int page, int size, String sort, String order, User user);

	// 특정 IDs 목록에 해당하는 레시피들 가져오기
	List<RecipeResponse> getRecipesByIds(List<Long> ids, User user);
	
	// 레시피 총 개수
	long getTotalRecipes();
	
	// 레시피 ID를 통해 레시피 가져오기 -> 상세보기
	RecipeResponse getRecipeById(Long id, User user);

	RecipeListResponse searchRecipes(String keyword, int page, User user);

	WeeklyRecipesResponse getRecipesByBudget(int minPrice, int maxPrice);

	void addRecommendedRecipe(List<RecommendedRecipeRequest> recipesRequest, User user);
}
