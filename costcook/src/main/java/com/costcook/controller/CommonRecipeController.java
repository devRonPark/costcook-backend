package com.costcook.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.response.RecipeListResponse;
import com.costcook.domain.response.RecipeResponse;
import com.costcook.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 모든 사용자가 조회 가능한 레시피 목록, 상세보기

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class CommonRecipeController {
	
	private final RecipeService recipeService;
	
	// 레시피 전체 목록 조회 
	@GetMapping(value = {"", "/"})
	public ResponseEntity<RecipeListResponse> getAllRecipe(
			@RequestParam(name = "page", defaultValue = "0") int page, 
			@RequestParam(name = "size", defaultValue = "9") int size, 
			@RequestParam(name = "sort", defaultValue = "createdAt") String sort,
			@RequestParam(name = "order", defaultValue = "desc") String order
			) {
		
		// 레시피 목록 가져오기
		List<RecipeResponse> recipes = recipeService.getRecipes(page, size, sort, order);
		
		// 총 레시피 개수 : 불러올 데이터가 없는 데 스크롤이 가능하면 계속해서 데이터를 불러옴 => 마지막 페이지를 설정해서 무한 로딩 방지
		long totalRecipes = recipeService.getTotalRecipes();
		// 총 페이지 수
		long totalPages = (long) Math.ceil((double) totalRecipes / size);
		
		// 응답할 데이터 구성
		RecipeListResponse response = new RecipeListResponse();
		response.setRecipes(recipes);
		response.setPage(page + 1);
		response.setSize(size);
		response.setTotalPages((int) totalPages);
		response.setTotalRecipes(totalRecipes);
		
		return ResponseEntity.ok(response);
	}
	
	
	// 레시피 상세보기
	@GetMapping("/{recipeId}")
	public ResponseEntity<RecipeResponse> getRecipe(@PathVariable("recipeId") Long id) {
		RecipeResponse recipeResponse = recipeService.getRecipeById(id);
		return ResponseEntity.ok(recipeResponse);
	}
	
	
	
}
