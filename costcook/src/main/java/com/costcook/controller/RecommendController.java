package com.costcook.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.RecipeUsageRequest;
import com.costcook.domain.request.RecommendedRecipeRequest;
import com.costcook.domain.response.BudgetRecipesResponse;
import com.costcook.domain.response.RecipeUsageResponse;
import com.costcook.entity.User;
import com.costcook.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendController {

	private final RecipeService recipeService;

	// 추천받는 레시피 중 예산에 맞는 레시피 들고오기
	@GetMapping("/recipes")
	public ResponseEntity<?> getRecipesByBudget( @RequestParam(value = "minBudget") int minBudget,
	        @RequestParam(value = "maxBudget") int maxBudget) {
		BudgetRecipesResponse response = recipeService.getRecipesByBudget(minBudget, maxBudget);
		return ResponseEntity.ok(response);
	}

	// 추천받은 레시피 중 선택한 레시피 DB에 저장
	@PostMapping("/selected-recipes")
	public ResponseEntity<Void> addWeeklyRecipes(@RequestBody List<RecommendedRecipeRequest> recipesRequest,
			@AuthenticationPrincipal User user) {
		recipeService.addRecommendedRecipe(recipesRequest, user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	
	// 레시피 사용상태 변경
	@PatchMapping("/recipes/{recipeId}/use")
	public ResponseEntity<?> modifyUseRecipe(@RequestBody RecipeUsageRequest recipeUsageRequest,  @AuthenticationPrincipal User user){
		RecipeUsageResponse result = recipeService.modifyUseRecipe(recipeUsageRequest, user);
		
		return ResponseEntity.ok(result);

	}
	
	
	// 추천받은 레시피 삭제
	@DeleteMapping("/selected-recipes")
	public ResponseEntity<Void> deleteWeeklyRecipes(@RequestBody RecommendedRecipeRequest recipesRequest,
			@AuthenticationPrincipal User user) {
		
		recipeService.deleteRecommendedRecipe(recipesRequest, user);
		return ResponseEntity.ok().build();
		
		
	}

}
