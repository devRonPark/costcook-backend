package com.costcook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.costcook.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

	List<Review> findAllByRecipeId(Long id);

	Optional<Review> findByRecipeIdAndUserId(Long id, Long reviewId);



}
