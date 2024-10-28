package com.costcook.service;

import java.util.List;

import com.costcook.domain.response.IngredientResponse;

public interface RecipeIngredientService {
	// 레시피 아이디에 따른 재료 목록 조회
	List<IngredientResponse> getRecipeIngredients(Long id);
}
