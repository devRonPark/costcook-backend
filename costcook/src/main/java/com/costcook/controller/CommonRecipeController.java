package com.costcook.controller;


import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.costcook.domain.response.IngredientResponse;
import com.costcook.domain.response.RecipeDetailResponse;
import com.costcook.domain.response.RecipeListResponse;
import com.costcook.domain.response.RecipeResponse;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.service.RecipeIngredientService;
import com.costcook.service.RecipeService;
import com.costcook.service.ReviewService;

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

	// 레시피 전체 목록 조회 
	@GetMapping(value = {"", "/"})
	public ResponseEntity<RecipeListResponse> getAllRecipe(
		@RequestParam(name = "page", defaultValue = "1") int page, 
		@RequestParam(name = "size", defaultValue = "9") int size, 
		@RequestParam(name = "sort", defaultValue = "createdAt") String sort,
		@RequestParam(name = "order", defaultValue = "desc") String order
	) {
		// 레시피 목록 가져오기
		RecipeListResponse response = recipeService.getRecipes(page, size, sort, order);
		return ResponseEntity.ok(response);
	}

	// 레시피 검색
	@GetMapping("/search")
	public ResponseEntity<RecipeListResponse> searchRecipes(
		@RequestParam(value = "keyword", required = false) String keyword,
		@RequestParam(value = "page", defaultValue = "0") int page
	) {
		log.info("레시피 검색 API 호출");
		RecipeListResponse response = recipeService.searchRecipes(keyword, page);
		return ResponseEntity.ok(response);
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
	
	// 레시피 리뷰 전체보기 (http://localhost:8080/api/recipes/1/reviews?page=0)
	@GetMapping("/{recipeId}/reviews")
	public ResponseEntity<ReviewListResponse> getRecipeReviews(
		@PathVariable("recipeId") Long recipeId,
		@RequestParam(name = "page", defaultValue = "1") int page,
		@RequestParam(name = "size", defaultValue = "3") int size
	) {
		// 리뷰 목록 가져오기
		ReviewListResponse response = reviewService.getReviewList(recipeId, page, size);
		return ResponseEntity.ok(response);		 
	}
	
	
	// 타 사이트 영역 가져오기
	@GetMapping("/test")
	   public ResponseEntity<?> testMan(@RequestParam("number") int number) {
	      String url = "https://m.10000recipe.com/recipe/" + number;
	      RestTemplate restTemplate = new RestTemplate();
	      String htmlContent = restTemplate.getForObject(url, String.class);
	      
	      Document doc = Jsoup.parse(htmlContent, "UTF-8");
	      Element specificTag = doc.selectFirst("ul.step_list");
	      
	      return ResponseEntity.ok().header("Content-type", "text/html; charset=UTF-8").body(specificTag != null ? specificTag.html() : "레시피 없음");
	   }
	
	
	
	
}
