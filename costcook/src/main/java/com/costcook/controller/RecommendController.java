package com.costcook.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.RecommendedRecipeRequest;
import com.costcook.domain.response.WeeklyRecipesResponse;
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

	// 예산에 맞는 레시피 들고오기
	@GetMapping("/recipes")
	public ResponseEntity<?> getRecipesByBudget( @RequestParam(value = "minBudget") int minBudget,
	        @RequestParam(value = "maxBudget") int maxBudget) {
		WeeklyRecipesResponse response = recipeService.getRecipesByBudget(minBudget, maxBudget);
		return ResponseEntity.ok(response);
	}

	// 레시피 DB에 저장
	@PostMapping("/selected-recipes")
	public ResponseEntity<Void> addWeeklyRecipes(@RequestBody List<RecommendedRecipeRequest> recipesRequest,
			@AuthenticationPrincipal User user) {
		log.info("Received request to add weekly recipes: {}", recipesRequest);
		recipeService.addRecommendedRecipe(recipesRequest, user);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
