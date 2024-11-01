package com.costcook.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.CreateFavoritesRequest;
import com.costcook.domain.response.CreateFavoritesResponse;
import com.costcook.domain.response.DeleteFavoriteResponse;
import com.costcook.domain.response.FavoriteListResponse;
import com.costcook.entity.User;
import com.costcook.service.FavoriteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

	private final FavoriteService favoriteService;
	
	// 즐겨찾기 조회
	@GetMapping(value = {"", "/"})
	public ResponseEntity<FavoriteListResponse> getFavoriteList(
		@RequestParam(name="page", defaultValue = "1") int page,
		@AuthenticationPrincipal User userDetails
	) {
		Long userId = userDetails.getId();
		// 즐겨찾기 목록 가져오기
		FavoriteListResponse response = favoriteService.getFavoritesByUserId(userId, page);
		return ResponseEntity.ok(response);
	}
	
	/**
     * 즐겨찾기 추가
     * @param CreateFavoriteRequest recipeId 가 포함된 Request DTO
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 추가된 즐겨찾기
     */
    @PostMapping(value = {"", "/"})
    public ResponseEntity<CreateFavoritesResponse> createFavorites(
		@RequestBody CreateFavoritesRequest request,
        @AuthenticationPrincipal User userDetails
	) {
        CreateFavoritesResponse response = favoriteService.createFavorites(userDetails, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
	
	// 즐겨찾기 삭제(취소)
	@DeleteMapping
    public ResponseEntity<DeleteFavoriteResponse> deleteFavorite(
		@RequestParam("recipeIds") List<Long> recipeIds, // 여러 개의 recipeId를 요청으로 받음
		@AuthenticationPrincipal User userDetails
	) {
		// recipeIds가 비어있으면 400 Bad Request 응답
		if (recipeIds.isEmpty()) {
			return ResponseEntity.badRequest().body(new DeleteFavoriteResponse("레시피 ID가 필요합니다.", Collections.emptyList()));
		}

		// 여러 개의 즐겨찾기를 삭제
		favoriteService.deleteFavorites(userDetails.getId(), recipeIds);

		// 삭제 성공 응답 구성
		DeleteFavoriteResponse response = new DeleteFavoriteResponse("즐겨찾기가 삭제되었습니다.", recipeIds);
		
		return ResponseEntity.ok(response); // 200 OK와 함께 응답
	}
}
