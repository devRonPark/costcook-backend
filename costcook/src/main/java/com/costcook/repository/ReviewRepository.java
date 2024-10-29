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

	List<Review> findAllByRecipeId(Long recipeId);
	
    @Modifying
    @Query("UPDATE Review r SET r.deletedAt = :deletedAt WHERE r.id = :id")
    void softDelete(@Param("id") Long id, @Param("deletedAt") LocalDateTime deletedAt);



	// 리뷰가 어떤 레시피의 리뷰인지 찾기
	Page<Review> findByRecipeId(Long id, Pageable pageable);

}
