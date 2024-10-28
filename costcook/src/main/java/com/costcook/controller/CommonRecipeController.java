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
import com.costcook.domain.response.ReviewResponse;
import com.costcook.service.RecipeService;
import com.costcook.service.ReviewService;
import com.costcook.domain.response.IngredientResponse;
import com.costcook.domain.response.RecipeDetailResponse;
import com.costcook.repository.RecipeIngredientRepository;
import com.costcook.service.RecipeIngredientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// 모든 사용자가 조회 가능한 레시피 목록, 상세보기

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class CommonRecipeController {
	
	private final RecipeService recipeService;
	private final ReviewService reviewService;
	
	private final RecipeIngredientService recipeIngredientService;
	private final RecipeIngredientRepository recipeIngredientRepository;

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
	
	// 레시피 리뷰 전체보기
	@GetMapping("/{recipeId}/reviews")
	public ResponseEntity<List<ReviewResponse>> getRecipeReviews(@PathVariable("recipeId") Long id){
		 List<ReviewResponse> reviewList = reviewService.getReviewList(id);
		 
		 if(reviewList.isEmpty()) {
			 return ResponseEntity.noContent().build();
		 }
		 return ResponseEntity.ok(reviewList);
		
	
	}
	// 레시피별 상세보기
	@GetMapping("/{recipeId}")
	public ResponseEntity<RecipeDetailResponse> getIngredientsByRecipeId(@PathVariable("recipeId") Long id) {
		// 아이디를 통한 레시피 조회 (레시피 아이디, 제목, 조회수, 설명, 평점, 리뷰개수, 북마크개수, 총 가격, 인분, 카테고리, 재료목록(재료아이디, 재료명, 가격, 단위, 수량, 재료카테고리))
		RecipeResponse recipeResponse = recipeService.getRecipeById(id);
		List<IngredientResponse> ingredients = recipeIngredientService.getRecipeIngredients(id);
		RecipeDetailResponse result = RecipeDetailResponse.toDTO(recipeResponse, ingredients);
		return ResponseEntity.ok(result);
	}



	
	
}
