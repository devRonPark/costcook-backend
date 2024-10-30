 package com.costcook.controller;
 
 import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.response.WeeklyRecipesResponse;
import com.costcook.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
 
 @Slf4j
 @RestController
 @RequestMapping("/api/recommendations/recipes")
 @RequiredArgsConstructor
 public class RecommendController {
	 
     private final RecipeService recipeService;
     
     @GetMapping("")
     public ResponseEntity<?> getRecipesByBudget(@RequestParam(value = "budget") int budget) {
    	 WeeklyRecipesResponse response = recipeService.getRecipesByBudget(budget);
    	 return ResponseEntity.ok(response);
     }
 }
