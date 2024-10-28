package com.costcook.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.costcook.domain.response.IngredientResponse;
import com.costcook.entity.Ingredient;
import com.costcook.entity.Recipe;
import com.costcook.entity.RecipeIngredient;
import com.costcook.repository.IngredientRepository;
import com.costcook.repository.RecipeIngredientRepository;
import com.costcook.repository.RecipeRepository;
import com.costcook.service.RecipeIngredientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeIngredientServiceImpl implements RecipeIngredientService {

	private final RecipeIngredientRepository recipeIngredientRepository;
	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;

	// 레시피, 재료
	public Recipe findRecipeById(Long id) {
		return recipeRepository.findById(id).orElse(null);
	}
	public Ingredient findIngredientById(Long id) {
		return ingredientRepository.findById(id).orElse(null);
	}
	
	@Override
	public List<IngredientResponse> getRecipeIngredients(Long id) {
		List<RecipeIngredient> recipeIngredient = recipeIngredientRepository.findByRecipeId(id);
		// RecipeIngredient -> Ingredient로 변환 
		List<IngredientResponse> ingredients = recipeIngredient.stream().map(ri ->
		IngredientResponse.builder()
				.ingredientId(ri.getIngredient().getId())
				.ingredientName(ri.getIngredient().getName())
				.category(ri.getIngredient().getCategory())
				.unitName(ri.getIngredient().getUnit().getName())
				.price(ri.getIngredient().getPrice())
				.quantity(ri.getQuantity())
				.build()
		).toList();
		System.out.println(ingredients);
		return ingredients;
	}

}
