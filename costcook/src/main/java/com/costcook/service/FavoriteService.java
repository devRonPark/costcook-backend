package com.costcook.service;

import com.costcook.domain.request.CreateFavoriteRequest;
import com.costcook.domain.response.FavoriteListResponse;
import com.costcook.domain.response.FavoriteResponse;
import com.costcook.entity.User;

public interface FavoriteService {

	// 유저ID에 맞는 즐겨찾기 목록 가져오기
	FavoriteListResponse getFavoritesByUserId(Long userId);

	// 즐겨찾기 추가
	FavoriteResponse createFavorite(User userDetails, CreateFavoriteRequest request);
}
