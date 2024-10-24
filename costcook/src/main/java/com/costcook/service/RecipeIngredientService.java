package com.costcook.service;

import java.util.List;

import com.costcook.domain.response.RecipeIngredientResponse;
import com.costcook.entity.RecipeIngredient;

public interface RecipeIngredientService {

	// DB에 직접 삽입
	void insertRecipeIngredients();

	// 재료 정보 조회 메소드
	List<RecipeIngredientResponse> getRecipeIngredients(Long id);

}
