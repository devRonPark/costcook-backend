package com.costcook.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.costcook.domain.ReviewStatsDTO;
import com.costcook.domain.response.RecipeResponse;
import com.costcook.entity.Category;
import com.costcook.entity.Recipe;
import com.costcook.repository.CategoryRepository;
import com.costcook.repository.RecipeIngredientRepository;
import com.costcook.repository.RecipeRepository;
import com.costcook.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
	
	private final RecipeRepository recipeRepository;
	private RecipeIngredientRepository recipeIngredientRepository;

	
	// 레시피 목록 조회
	@Override
	public List<RecipeResponse> getRecipes(int page, int size, String sort, String order) {
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Recipe> recipePage;
		
		// 정렬
        if (sort.equals("viewCount")) {
        	if (order.equals("asc")) { // 오름차순
        		recipePage = recipeRepository.findAllByOrderByViewCountAsc(pageable);
        	} else { // 내림차순
        		recipePage = recipeRepository.findAllByOrderByViewCountDesc(pageable);
        	}
        } else if (sort.equals("avgRatings")) {
        	 if (order.equals("asc")) { // 오름차순
        	 	recipePage = recipeRepository.findAllOrderByAverageScoreAsc(pageable);
        	 } else { // 내림차순
        	 	recipePage = recipeRepository.findAllOrderByAverageScoreDesc(pageable);        		
        	 }
        } else { // 생성일(디폴트)
        	if (order.equals("asc")) { // 오름차순
        		recipePage = recipeRepository.findAllByOrderByCreatedAtAsc(pageable);
        	} else {
        		recipePage = recipeRepository.findAllByOrderByCreatedAtDesc(pageable);        		
        	}
        }
        
        // 아이디, 이름, 이미지, [가격], [평점], [조회수]
//		응답할 데이터 
		return recipePage.getContent().stream()
				.map(recipe -> {
					ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipe.getId());
					Long totalPrice = recipeRepository.getTotalPrice(recipe.getId());
					double averageScore = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
					int commentCount = stats != null && stats.getReviewCount() != null ? stats.getReviewCount().intValue() : 0;
					return RecipeResponse.toDTO(recipe, averageScore, commentCount, totalPrice);
				})
				.toList();
		
		
}
	
	
	// 전체 레시피 수 조회 : 총 페이지를 미리 입력하여, 무한 로딩 방지
	@Override
	public long getTotalRecipes() {
		return recipeRepository.count();
	}

	
	// 레시피 상세 조회
	@Override
	@Transactional
	public RecipeResponse getRecipeById(Long id) {
		Recipe recipe = recipeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("레시피 정보가 없습니다."));
		recipeRepository.updateViewCount(id);
		// 리뷰 평점 가져오기
		ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(id);
		double averageScore = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
		int commentCount = stats != null && stats.getReviewCount() != null ? stats.getReviewCount().intValue() : 0;
		
		// 총 금액 가져오기
		Long totalPrice = recipeRepository.getTotalPrice(recipe.getId());
		// 리뷰 개수 가져오기
		return RecipeResponse.toDTO(recipe, averageScore, commentCount, totalPrice);
	}
}
