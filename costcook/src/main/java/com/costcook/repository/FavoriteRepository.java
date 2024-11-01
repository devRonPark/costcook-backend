package com.costcook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.costcook.entity.Favorite;
import com.costcook.entity.FavoriteId;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

	List<Favorite> findByUserId(Long userId);
	boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);
}
