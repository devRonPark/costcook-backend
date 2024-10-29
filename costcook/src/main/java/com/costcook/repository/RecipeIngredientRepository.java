package com.costcook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.costcook.entity.RecipeIngredient;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

	// 레시피 당 재료 테이블에서 레시피ID를 찾기
	List<RecipeIngredient> findByRecipeId(Long recipeId);

  // 특정 레시피에 해당하는 행들 삭제하기
  @Modifying
  @Query("DELETE FROM RecipeIngredient ri WHERE ri.recipe.id = :recipeId")
  void deleteByRecipeId(@Param("recipeId") Long recipeId);

}
