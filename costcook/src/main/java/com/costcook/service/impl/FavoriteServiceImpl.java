package com.costcook.service.impl;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.costcook.domain.ReviewStatsDTO;
import com.costcook.domain.request.CreateFavoriteRequest;
import com.costcook.domain.response.FavoriteListResponse;
import com.costcook.domain.response.FavoriteResponse;
import com.costcook.entity.Favorite;
import com.costcook.entity.FavoriteId;
import com.costcook.entity.Recipe;
import com.costcook.entity.User;
import com.costcook.exceptions.AlreadyExistsException;
import com.costcook.repository.FavoriteRepository;
import com.costcook.repository.RecipeRepository;
import com.costcook.service.FavoriteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
	private final RecipeRepository recipeRepository;
	private final FavoriteRepository favoriteRepository;
	
	// 유저ID로 유저가 작성한 즐겨찾기 목록 가져오기
	@Override
    public FavoriteListResponse getFavoritesByUserId(Long userId, int page) {
		log.info("{}", userId);
		int validPage = Math.max(page, 1) - 1; // 최소 페이지 설정: 1부터
		int size = 9;
		Pageable pageable = PageRequest.of(validPage, size); // 페이지는 0부터 시작

		Page<Favorite> favoritePage = favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    	log.info("{}", favoritePage.getTotalElements());
		// 응답할 데이터
		return FavoriteListResponse.builder()
			.page(page)
			.size(favoritePage.getContent().size()) // 현재 페이지의 즐겨찾기 수
			.totalPages(favoritePage.getTotalPages())
			.totalFavorites(favoritePage.getTotalElements())
			.favorites(
				favoritePage.getContent().stream().map(favorite -> {
					// 레시피에 대한 통계 및 가격 정보 가져오기
					ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(favorite.getRecipe().getId());
					int price = recipeRepository.getTotalPrice(favorite.getRecipe().getId());
					double avgRatings = (stats != null && stats.getAverageScore() != null) ? stats.getAverageScore() : 0.0;

					// FavoriteResponse DTO로 변환
					return FavoriteResponse.toDTO(favorite, price, avgRatings);
				}).collect(Collectors.toList()) // 스트림 결과를 리스트로 수집
			)
			.build();
    }

	@Override
	public FavoriteResponse createFavorite(User userDetails, CreateFavoriteRequest request) {
		log.info("Request DTO 유효성 검증 시작");
	    if (request.getRecipeId() == null) {
			throw new IllegalArgumentException("recipeId 는 필수 값입니다.");
		}
		Long recipeId = request.getRecipeId();
		log.info("Request DTO 유효성 검증 완료");

		log.info("이미 존재하는 즐겨찾기인지 확인 시작");
		// 이미 존재하는 즐겨찾기인지 확인
        if (favoriteRepository.existsByUserIdAndRecipeId(userDetails.getId(), recipeId)) {
            throw new AlreadyExistsException("이미 즐겨찾기에 추가된 레시피입니다.");
        }
		log.info("이미 존재하는 즐겨찾기인지 확인 완료");
		
		log.info("recipeId로 Recipe 엔티티 조회 시작");
		// recipeId로 Recipe 엔티티 조회
		Recipe recipe = recipeRepository.findById(recipeId)
		.orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
		log.info("recipeId로 Recipe 엔티티 조회 완료");

		ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipe.getId());
    	int price = recipeRepository.getTotalPrice(recipe.getId());
    	double avgRatings = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;

		// 즐겨찾기 추가
		Favorite favorite = Favorite.builder()
				.id(new FavoriteId(userDetails.getId(), recipeId))
				.user(userDetails)
				.recipe(recipe)
				.build();
		
		favoriteRepository.save(favorite);

		// TODO: recipe 의 price 및 avgRatings 어떻게 계산해서 반영할 지 고민
		return FavoriteResponse.toDTO(favorite, price, avgRatings);
	}
}
