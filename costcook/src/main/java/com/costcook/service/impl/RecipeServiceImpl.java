package com.costcook.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.costcook.domain.ReviewStatsDTO;
import com.costcook.domain.request.RecommendedRecipeRequest;
import com.costcook.domain.response.BudgetRecipesResponse;
import com.costcook.domain.response.RecipeListResponse;
import com.costcook.domain.response.RecipeResponse;
import com.costcook.domain.response.WeeklyRecipesResponse;
import com.costcook.entity.Recipe;
import com.costcook.entity.User;
import com.costcook.repository.FavoriteRepository;
import com.costcook.entity.RecommendedRecipe;
import com.costcook.repository.RecipeIngredientRepository;
import com.costcook.repository.RecipeRepository;
import com.costcook.repository.RecommendedRecipeRepository;
import com.costcook.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
	private final RecipeRepository recipeRepository;
	private final RecipeIngredientRepository recipeIngredientRepository;
	private final FavoriteRepository favoriteRepository;
	private final RecommendedRecipeRepository recommendedRecipeRepository;

	// 레시피 목록 조회
	@Override
	public RecipeListResponse getRecipes(int page, int size, String sort, String order, User user) {
		
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
        	.size(size)
        	.totalPages(recipePage.getTotalPages())
        	.totalRecipes(recipePage.getTotalElements())
        	.recipes(
        		// 아이디, 이름, 이미지, [가격], [평점], [조회수]
        		recipePage.getContent().stream()
    				.map(recipe -> {
    					ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipe.getId());
    					int totalPrice = recipeRepository.getTotalPrice(recipe.getId());
    					double averageScore = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
    					int reviewCount = stats != null && stats.getReviewCount() != null ? stats.getReviewCount().intValue() : 0;
						boolean isFavorite = user != null ? favoriteRepository.existsByUserIdAndRecipeIdAndDeletedAtIsNull(user.getId(), recipe.getId()) : false;
    					return RecipeResponse.toDTO(recipe, averageScore, reviewCount, totalPrice, isFavorite);
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
	public RecipeResponse getRecipeById(Long id, User user) {
		Recipe recipe = recipeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("레시피 정보가 없습니다."));
		// 조회수 증가
		recipeRepository.updateViewCount(id);
		// 리뷰 평점 가져오기
		ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(id);
		double averageScore = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
		int commentCount = stats != null && stats.getReviewCount() != null ? stats.getReviewCount().intValue() : 0;

		boolean isFavorite = user != null ? favoriteRepository.existsByUserIdAndRecipeIdAndDeletedAtIsNull(user.getId(), recipe.getId()) : false;
		
		// 총 금액 가져오기
		int totalPrice = recipeRepository.getTotalPrice(recipe.getId());
		// 레시피 테이블에 가격 반영
		recipe.setPrice(totalPrice);
		// 리뷰 개수 가져오기
		return RecipeResponse.toDTO(recipe, averageScore, commentCount, totalPrice, isFavorite);
	}

	@Override
	public RecipeListResponse searchRecipes(String keyword, int page, User user) {
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

			return RecipeListResponse.builder().page(validPage + 1).size(recipePage.getNumberOfElements())
					.totalPages(recipePage.getTotalPages()).totalRecipes(recipePage.getTotalElements())
					.recipes(recipePage.getContent().stream().map(recipe -> {
						ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipe.getId());
						int totalPrice = recipeRepository.getTotalPrice(recipe.getId());
						double averageScore = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
						int commentCount = stats != null && stats.getReviewCount() != null ? stats.getReviewCount().intValue() : 0;
						boolean isFavorite = user != null ? favoriteRepository.existsByUserIdAndRecipeIdAndDeletedAtIsNull(user.getId(), recipe.getId()) : false;
						return RecipeResponse.toDTO(recipe, averageScore, commentCount, totalPrice, isFavorite);
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

	@Override
	public BudgetRecipesResponse getRecipesByBudget(int minPrice, int maxPrice) {
		List<Recipe> recipes = recipeRepository.findByPriceRange(minPrice, maxPrice);

		// Recipe를 WeeklyRecipesResponse.Recipe으로 변환
		List<BudgetRecipesResponse.Recipe> recipeList = recipes.stream().map(recipe -> {
			// 리뷰 통계 가져오기
			ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipe.getId());

			// 평균 평점 계산
			double averageScore = (stats != null && stats.getAverageScore() != null) ? stats.getAverageScore() : 0.0;
			int favoriteCount = (stats != null && stats.getReviewCount() != null) ? stats.getReviewCount().intValue()
					: 0;

			return BudgetRecipesResponse.Recipe.builder().id(recipe.getId()).title(recipe.getTitle())
					.thumbnailUrl(recipe.getThumbnailUrl()).price(recipe.getPrice()).favoriteCount(favoriteCount) // 계산된
																													// 즐겨찾기
																													// 개수
					.avgRatings(Math.round(averageScore * 10) / 10.0) // 평점을 소수점 첫째자리까지 반올림
					.build();
		}).collect(Collectors.toList());

		Collections.shuffle(recipeList);

		// WeeklyRecipesResponse 반환
		return BudgetRecipesResponse.builder().budget(maxPrice).recipes(recipeList).build();
	}

	@Override
	public void addRecommendedRecipe(List<RecommendedRecipeRequest> recipesRequest, User user) {
		List<RecommendedRecipe> recipes = recipesRequest.stream().map((RecommendedRecipeRequest request) -> { // 타입을
																												// 명시적으로
																												// 지정
			Recipe recipe = recipeRepository.findById(request.getRecipeId())
					.orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다. ID: " + request.getRecipeId()));
			return RecommendedRecipe.builder().year(request.getYear()).weekNumber(request.getWeekNumber())
					.isUsed(request.isUsed()).recipe(recipe).user(user).build();
		}).collect(Collectors.toList());

		recommendedRecipeRepository.saveAll(recipes);
	}

	@Override
	public List<WeeklyRecipesResponse.Recipe> getRecommendedRecipes(int year, int weekNumber, User user) {
		// 추천 레시피를 가져옵니다.
		List<RecommendedRecipe> recommendedRecipes = recommendedRecipeRepository.findByYearAndWeekNumberAndUserId(year,
				weekNumber, user.getId());

		// 추천 레시피를 DTO로 변환
		return recommendedRecipes.stream().map((RecommendedRecipe recommendedRecipe) -> {
			Recipe recipe = recommendedRecipe.getRecipe(); // 추천 레시피에서 실제 레시피 정보 가져오기

			// 리뷰 통계 가져오기
			ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipe.getId());

			// 평균 평점 및 북마크 수 계산
			double averageScore = (stats != null && stats.getAverageScore() != null) ? stats.getAverageScore() : 0.0;
			int favoriteCount = (stats != null && stats.getReviewCount() != null) ? stats.getReviewCount().intValue()
					: 0;

			return WeeklyRecipesResponse.Recipe.builder() // WeeklyRecipesResponse.Recipe로 반환
					.id(recipe.getId()).title(recipe.getTitle()).thumbnailUrl(recipe.getThumbnailUrl())
					.price(recipe.getPrice()).favoriteCount(favoriteCount) // 북마크 수
					.avgRatings(Math.round(averageScore * 10) / 10.0) // 평점을 소수점 첫째자리까지 반올림
					.build();
		}).collect(Collectors.toList());
	}

}
