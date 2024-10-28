package com.costcook.service;

import java.util.List;
import com.costcook.domain.response.RecipeResponse;

public interface RecipeService {

	// 레시피 목록 가져오기
	List<RecipeResponse> getRecipes(int page, int size, String sort, String order);
	
	// 레시피 총 개수
	long getTotalRecipes();
	
	// 레시피 ID를 통해 레시피 가져오기 -> 상세보기
	RecipeResponse getRecipeById(Long id);
}
