package com.costcook.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.costcook.entity.Review;
import com.costcook.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    // 특정 recipeId를 가진 모든 리뷰 목록 조회
	List<Review> findAllByRecipeId(Long recipeId);
	
    // soft delete 구현을 위한 메서드 (deletedAt 필드를 업데이트하여 삭제 처리)
    @Modifying
    @Query("UPDATE Review r SET r.deletedAt = :deletedAt WHERE r.id = :id")
    void softDelete(@Param("id") Long id, @Param("deletedAt") LocalDateTime deletedAt);

    // 특정 userId를 가진 리뷰 목록을 페이지 단위로 조회 (page와 size 활용)
    Page<Review> findAllByUserId(Long userId, Pageable pageable);
}
