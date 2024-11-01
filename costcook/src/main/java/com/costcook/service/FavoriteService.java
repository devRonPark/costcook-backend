package com.costcook.service;

import java.util.List;

import com.costcook.domain.request.CreateFavoritesRequest;
import com.costcook.domain.response.CreateFavoritesResponse;
import com.costcook.domain.response.FavoriteListResponse;
import com.costcook.entity.User;

public interface FavoriteService {

	// 유저ID에 맞는 즐겨찾기 목록 가져오기
	FavoriteListResponse getFavoritesByUserId(Long userId, int page);

	// 즐겨찾기 추가
	CreateFavoritesResponse createFavorites(User userDetails, CreateFavoritesRequest request);

	// 즐겨찾기 삭제
	void deleteFavorites(Long userId, List<Long> recipeIds);
}
