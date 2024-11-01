package com.costcook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.CreateFavoriteRequest;
import com.costcook.domain.response.FavoriteListResponse;
import com.costcook.domain.response.FavoriteResponse;
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
	public ResponseEntity<FavoriteListResponse> getAllFavorite(@RequestParam Long userId) {
		// 즐겨찾기 목록 가져오기
		FavoriteListResponse response = favoriteService.getFavoritesByUserId(userId);
		return ResponseEntity.ok(response);
	}
	
	
	/**
     * 즐겨찾기 추가
     * @param CreateFavoriteRequest recipeId 가 포함된 Request DTO
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 추가된 즐겨찾기
     */
    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(
		@RequestBody CreateFavoriteRequest request,
        @AuthenticationPrincipal User userDetails
	) {
        FavoriteResponse response = favoriteService.createFavorite(userDetails, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
	
	
	
	// 즐겨찾기 삭제(취소)
	
	
	
	
}
