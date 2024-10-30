package com.costcook.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.costcook.domain.ReviewStatsDTO;
import com.costcook.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

	// 조회수 정렬
	Page<Recipe> findAllByOrderByViewCountDesc(Pageable pageable);
	Page<Recipe> findAllByOrderByViewCountAsc(Pageable pageable);

	// 평점 정렬
	@Query("SELECT recipe FROM Recipe recipe LEFT JOIN Review review ON recipe.id = review.recipe.id GROUP BY recipe.id ORDER BY AVG(review.score) ASC")
	Page<Recipe> findAllOrderByAverageScoreAsc(Pageable pageable);
	@Query("SELECT recipe FROM Recipe recipe LEFT JOIN Review review ON recipe.id = review.recipe.id GROUP BY recipe.id ORDER BY AVG(review.score) DESC")
	Page<Recipe> findAllOrderByAverageScoreDesc(Pageable pageable);

	// 디폴트 (생성일) 정렬
	Page<Recipe> findAllByOrderByCreatedAtDesc(Pageable pageable);
	Page<Recipe> findAllByOrderByCreatedAtAsc(Pageable pageable);
	
	@Modifying
	@Query("UPDATE Recipe r SET r.viewCount = r.viewCount + 1 WHERE r.id = :id")
	void updateViewCount(@Param("id") Long id);
	
	@Query("SELECT new com.costcook.domain.ReviewStatsDTO(COUNT(r), AVG(r.score)) FROM Review r WHERE r.recipe.id = :id")
	ReviewStatsDTO findCountAndAverageScoreByRecipeId(@Param("id") Long id);

	@Query("SELECT SUM(i.price * ri.quantity) FROM Recipe recipe LEFT JOIN RecipeIngredient ri ON ri.recipe.id = recipe.id LEFT JOIN Ingredient i on ri.ingredient.id = i.id WHERE recipe.id = :id")
	Long getTotalPrice(@Param("id") Long id);
	
	
}
