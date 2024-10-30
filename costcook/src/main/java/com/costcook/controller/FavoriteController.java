package com.costcook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.response.FavoriteListResponse;
import com.costcook.domain.response.FavoriteResponse;
import com.costcook.service.FavoriteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favories")
public class FavoriteController {

	private final FavoriteService favoriteService;
	
	// 즐겨찾기 조회
	@GetMapping(value = {"", "/"})
	public ResponseEntity<FavoriteListResponse> getAllFavorite(@RequestParam Long userId) {
		// 즐겨찾기 목록 가져오기
		FavoriteListResponse response = favoriteService.getFavoritesByUserId(userId);
		return ResponseEntity.ok(response);
	}
	
	
	// 즐겨찾기 추가(등록)
	
	
	
	// 즐겨찾기 삭제(취소)
	
	
	
	
}
