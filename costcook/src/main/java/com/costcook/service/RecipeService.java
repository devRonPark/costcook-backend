package com.costcook.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.costcook.domain.response.RecipeResponse;

public interface RecipeService {

	// 레시피 목록 가져오기
	List<RecipeResponse> getRecipes(int page, int size, String sort, String order);
	
	// 레시피 총 개수
	long getTotalRecipes();
	
	// 레시피 ID 가져오기
	RecipeResponse getRecipeById(Long id);

	// DB에 직접 데이터를 넣는 메소드
	void insertRecipes();


}
