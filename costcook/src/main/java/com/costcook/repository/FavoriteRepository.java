package com.costcook.repository;

import java.util.List;
import java.util.Optional;

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
	
	// deletedAt이 null인 경우만 존재 여부 확인
    boolean existsByUserIdAndRecipeIdAndDeletedAtIsNull(Long userId, Long recipeId);

	// 즐겨찾기 목록 생성일 기준 최신 순부터 조회
	@Query("SELECT fav FROM Favorite fav WHERE fav.id.userId = :userId AND fav.deletedAt IS NULL")
    Page<Favorite> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);
	Optional<Favorite> findByUserIdAndRecipeId(@Param("userId") Long userId, @Param("recipeId") Long recipeId);
}
