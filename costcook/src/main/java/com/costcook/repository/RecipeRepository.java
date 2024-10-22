package com.costcook.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.costcook.entity.RecipeItem;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeItem, Long> {

	// 조회수 정렬
	Page<RecipeItem> findAllByOrderByViewCountDesc(Pageable pageable);
	Page<RecipeItem> findAllByOrderByViewCountAsc(Pageable pageable);

	// 평점 정렬
	Page<RecipeItem> findAllByOrderByAvgRatingsDesc(Pageable pageable);
	Page<RecipeItem> findAllByOrderByAvgRatingsAsc(Pageable pageable);

	// 디폴트 (생성일) 정렬
	Page<RecipeItem> findAllByOrderByCreatedAtDesc(Pageable pageable);
	Page<RecipeItem> findAllByOrderByCreatedAtAsc(Pageable pageable);




	
}
