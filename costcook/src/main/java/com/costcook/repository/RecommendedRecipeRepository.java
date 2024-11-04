package com.costcook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.costcook.entity.Budget;
import com.costcook.entity.RecommendedRecipe;
import com.costcook.entity.User;

public interface RecommendedRecipeRepository extends JpaRepository<RecommendedRecipe, Long>{

	List<RecommendedRecipe> findByYearAndWeekNumberAndUserId(int year, int weekNumber, Long id);

	RecommendedRecipe findByYearAndWeekNumberAndUserIdAndRecipeId(int year, int weekNumber, Long id, Long recipeId);

}
