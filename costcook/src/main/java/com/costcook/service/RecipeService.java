package com.costcook.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.costcook.domain.response.RecipeResponse;

public interface RecipeService {

	List<RecipeResponse> getRecipes(int page, int size, String sort, String order);
	
	long getTotalRecipes();
	
//	RecipeResponse insertRecipe(RecipeRequest recipeRequest, MultipartFile file);
	
	RecipeResponse getRecipeById(Long id);


//	RecipeResponse updateRecipe(RecipeRequest recipeRequest, Long id, MultipartFile file);

//	List<RecipeResponse> getAdminRecipes(int page, int limit);


	// DB에 직접 데이터를 넣는 메소드
	void insertRecipes();



}
