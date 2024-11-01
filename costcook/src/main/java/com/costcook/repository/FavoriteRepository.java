package com.costcook.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.costcook.entity.Favorite;
import com.costcook.entity.FavoriteId;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

	List<Favorite> findByUserId(Long userId);
	boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

	// 즐겨찾기 목록 생성일 기준 최신 순부터 조회
	@Query("SELECT fav FROM Favorite fav WHERE fav.id.userId = :userId AND fav.deletedAt IS NULL")
    Page<Favorite> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);
}
