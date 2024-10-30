package com.costcook.service.impl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.costcook.domain.ReviewStatsDTO;
import com.costcook.domain.response.RecipeListResponse;
import com.costcook.domain.response.RecipeResponse;
import com.costcook.entity.Recipe;
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
	private final RecipeIngredientRepository recipeIngredientRepository;

	// 레시피 목록 조회
	@Override
	public RecipeListResponse getRecipes(int page, int size, String sort, String order) {
		
		Pageable pageable = PageRequest.of(page - 1, size);
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
        
		// 응답할 데이터 
        return RecipeListResponse.builder()
        	.page(page)
        	.size(recipePage.getNumberOfElements())
        	.totalPages(recipePage.getTotalPages())
        	.totalRecipes(recipePage.getTotalElements())
        	.recipes(
        		// 아이디, 이름, 이미지, [가격], [평점], [조회수]
        		recipePage.getContent().stream()
    				.map(recipe -> {
    					ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipe.getId());
    					Long totalPrice = recipeRepository.getTotalPrice(recipe.getId());
    					double averageScore = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
    					int reviewCount = stats != null && stats.getReviewCount() != null ? stats.getReviewCount().intValue() : 0;
    					return RecipeResponse.toDTO(recipe, averageScore, reviewCount, totalPrice);
    				})
    				.toList())
        	.build();
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
		// 조회수 증가
		recipeRepository.updateViewCount(id);
		// 리뷰 평점 가져오기
		ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(id);
		double averageScore = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
		int commentCount = stats != null && stats.getReviewCount() != null ? stats.getReviewCount().intValue() : 0;
		
		// 총 금액 가져오기
		Long totalPrice = recipeRepository.getTotalPrice(recipe.getId());
		// 레시피 테이블에 가격 반영
		recipe.setPrice(totalPrice.intValue());
		
		// 리뷰 개수 가져오기
		return RecipeResponse.toDTO(recipe, averageScore, commentCount, totalPrice);
	}

	@Override
	public RecipeListResponse searchRecipes(String keyword, int page) {
		try {
			// 예외 처리: keyword가 null이거나 빈 문자열 또는 공백만 있는 경우
			if (keyword == null || keyword.trim().isEmpty()) {
				throw new IllegalArgumentException("검색어를 입력해 주세요.");
			}

			// 기본 페이지 크기 설정
			int size = 9; 
			// 최소 1 이상의 값을 보장
			int validPage = Math.max(page, 1) - 1; 
	
			// 페이지네이션 설정
			Pageable pageable = PageRequest.of(validPage, size);
		
			// 공백을 제거한 후 검색을 수행
			String trimmedKeyword = keyword.trim();
		
            // 새로운 요구사항: keyword, page 로 검색된 레시피 목록 중에서 레시피를 구성하는 재료명까지도 검색 대상에 포함시키고 싶어.

			// 키워드를 포함하는 레시피 검색 로직
			Page<Recipe> recipePage = recipeRepository.findByTitleOrIngredientNameContaining(trimmedKeyword, pageable);
			
			return RecipeListResponse.builder()
				.page(validPage + 1)
				.size(recipePage.getNumberOfElements())
				.totalPages(recipePage.getTotalPages())
				.totalRecipes(recipePage.getTotalElements())
				.recipes(
					recipePage.getContent().stream()
					.map(recipe -> {
						ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipe.getId());
						Long totalPrice = recipeRepository.getTotalPrice(recipe.getId());
						double averageScore = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
						int commentCount = stats != null && stats.getReviewCount() != null ? stats.getReviewCount().intValue() : 0;
						return RecipeResponse.toDTO(recipe, averageScore, commentCount, totalPrice);
					})
					.toList()
				)
				.build();
		} catch (IllegalArgumentException e) {
			log.error("잘못된 검색어가 입력되었습니다: {}", keyword, e);
			throw e;
		} catch (Exception e) {
			log.error("레시피 검색 중 오류가 발생했습니다. 검색어: {}", keyword, e);
			throw e;
		}
	}
}
