package com.costcook.service;

import java.util.List;

import com.costcook.domain.response.RecipeResponse;

public interface RecipeService {

	List<RecipeResponse> getAllRecipe();

	RecipeResponse getRecipeById(Long id);

}
