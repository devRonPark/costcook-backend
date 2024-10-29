package com.costcook.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.costcook.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

	List<Review> findAllByRecipeId(Long id);

	// 리뷰가 어떤 레시피의 리뷰인지 찾기
	Page<Review> findByRecipeId(Long id, Pageable pageable);

}
