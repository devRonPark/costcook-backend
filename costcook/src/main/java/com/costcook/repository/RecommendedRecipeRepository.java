package com.costcook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.costcook.entity.Budget;
import com.costcook.entity.RecommendedRecipe;
import com.costcook.entity.User;

public interface RecommendedRecipeRepository extends JpaRepository<RecommendedRecipe, Long>{

	// 사용한 레시피 ID 조회
	@Query("SELECT r.recipe.id FROM RecommendedRecipe r WHERE r.user = :user AND r.year = :year AND r.weekNumber = :weekNumber AND r.isUsed = :isUsed")
	List<Long> findRecipeIdsByUserAndYearAndWeekNumberAndIsUsed(@Param("user") User user, 
	                                                             @Param("year") int year, 
	                                                             @Param("weekNumber") int weekNumber, 
	                                                             @Param("isUsed") boolean isUsed);
}
