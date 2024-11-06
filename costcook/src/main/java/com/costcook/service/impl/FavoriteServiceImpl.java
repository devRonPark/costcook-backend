package com.costcook.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.costcook.domain.ReviewStatsDTO;
import com.costcook.domain.request.CreateFavoritesRequest;
import com.costcook.domain.response.CreateFavoritesResponse;
import com.costcook.domain.response.FavoriteListResponse;
import com.costcook.domain.response.FavoriteResponse;
import com.costcook.entity.Favorite;
import com.costcook.entity.FavoriteId;
import com.costcook.entity.Recipe;
import com.costcook.entity.User;
import com.costcook.exceptions.NotFoundException;
import com.costcook.repository.FavoriteRepository;
import com.costcook.repository.RecipeRepository;
import com.costcook.service.FavoriteService;

import jakarta.transaction.Transactional;
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
	@Transactional
	public CreateFavoritesResponse createFavorites(User userDetails, CreateFavoritesRequest request) {
		List<FavoriteResponse> addedFavorites = new ArrayList<>();
	
		log.info("{}", request.getRecipeIds());
		for (Long recipeId : request.getRecipeIds()) {
			// 이미 존재하는 즐겨찾기인지 확인
			Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndRecipeId(userDetails.getId(), recipeId);
	
			if (!existingFavorite.isPresent()) {
				// recipeId로 Recipe 엔티티 조회
				Recipe recipe = recipeRepository.findById(recipeId)
					.orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
	
				int price = recipeRepository.getTotalPrice(recipe.getId());
				ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipe.getId());
				double avgRatings = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
	
				// 즐겨찾기 추가
				Favorite favorite = Favorite.builder()
						.id(new FavoriteId(userDetails.getId(), recipeId))
						.user(userDetails)
						.recipe(recipe)
						.build();
				
				favoriteRepository.save(favorite);
	
				// FavoriteResponse로 변환하여 추가
				FavoriteResponse favoriteResponse = FavoriteResponse.toDTO(favorite, price, avgRatings);
				addedFavorites.add(favoriteResponse);
			} else {
				log.info("이미 존재하는 즐겨찾기지만 삭제된 상태인 경우");
				// 이미 존재하는 즐겨찾기지만 삭제된 상태인 경우
				Favorite favorite = existingFavorite.get();
				
				log.info("{}", favorite.toString());
				if (favorite.getDeletedAt() != null) {
					// deletedAt을 null로 설정하여 즐겨찾기 복원
					log.info("deletedAt을 null로 설정하여 즐겨찾기 복원");
					favorite.setDeletedAt(null);
					favoriteRepository.save(favorite); // 변경 사항 저장
					
					// 레시피 정보 업데이트 (필요한 경우)
					int price = recipeRepository.getTotalPrice(recipeId);
					ReviewStatsDTO stats = recipeRepository.findCountAndAverageScoreByRecipeId(recipeId);
					double avgRatings = stats != null && stats.getAverageScore() != null ? stats.getAverageScore() : 0.0;
					
					// FavoriteResponse로 변환하여 추가
					FavoriteResponse favoriteResponse = FavoriteResponse.toDTO(favorite, price, avgRatings);
					addedFavorites.add(favoriteResponse);
				}
			}
		}
		return new CreateFavoritesResponse(addedFavorites); // 추가된 FavoriteResponse 목록 반환
	}
	
	@Override
	public void deleteFavorites(Long userId, List<Long> recipeIds) {
		for (Long recipeId : recipeIds) {
			// 즐겨찾기 엔티티를 찾기
			FavoriteId favoriteId = new FavoriteId(userId, recipeId);
			Favorite favorite = favoriteRepository.findById(favoriteId)
				.orElseThrow(() -> new NotFoundException("해당 즐겨찾기를 찾을 수 없습니다."));
	
			// 즐겨찾기 삭제
			favorite.softDelete(); // 소프트 삭제 메소드 호출
			favoriteRepository.save(favorite);
		}
	}
}
