package com.costcook.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.costcook.domain.response.RecipeResponse;
import com.costcook.repository.RecipeRepository;
import com.costcook.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
	
	private final RecipeRepository recipeRepository;

	@Override
	public List<RecipeResponse> getAllRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecipeResponse getRecipeById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
