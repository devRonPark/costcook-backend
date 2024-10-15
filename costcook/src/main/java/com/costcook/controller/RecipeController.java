package com.costcook.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.response.RecipeResponse;
import com.costcook.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipies")
public class RecipeController {
	
	private final RecipeService recipeService;
	
	
	// 레시피 전체 목록 조회
	@GetMapping("")
	public ResponseEntity<List<RecipeResponse>> getAllRecipe(@RequestParam(name = "id", required = false) Long id) {
		List<RecipeResponse> result = new ArrayList<>();
		// 레시피 ID가 없으면 모든 레시피 조회, 있으면 리스트에 추가
		if (id == null) {
			result = recipeService.getAllRecipe();
		} else {
			RecipeResponse RecipeResponse = recipeService.getRecipeById(id);
			result.add(RecipeResponse);
		}
		return ResponseEntity.ok(result);
	}
	
	

}
