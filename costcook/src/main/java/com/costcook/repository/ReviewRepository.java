package com.costcook.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.costcook.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    // 특정 recipeId를 가진 모든 리뷰 목록 조회
	List<Review> findAllByRecipeId(Long recipeId);
	
    // soft delete 구현을 위한 메서드 (deletedAt 필드를 업데이트하여 삭제 처리)
    @Modifying
    @Query("UPDATE Review r SET r.deletedAt = :deletedAt WHERE r.id = :id")
    void softDelete(@Param("id") Long id, @Param("deletedAt") LocalDateTime deletedAt);

	Page<Review> findByRecipeId(Long recipeId, Pageable pageable);

	// 리뷰 목록 생성일 순으로 정렬
    @Query("SELECT r FROM Review r WHERE r.recipe.id = :recipeId ORDER BY r.createdAt DESC")
    Page<Review> findByRecipeIdOrderByCreatedAtDesc(@Param("recipeId") Long recipeId, Pageable pageable);

    // 특정 userId를 가진 리뷰 목록을 페이지 단위로 조회 (page와 size 활용)
    Page<Review> findAllByUserId(Long userId, Pageable pageable);
}
