package com.costcook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.costcook.entity.RecipeIngredient;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

	// 레시피 당 재료 테이블에서 레시피ID를 찾기
	List<RecipeIngredient> findByRecipeId(Long recipeId);

}
