package com.costcook.service;

import com.costcook.domain.response.FavoriteListResponse;

public interface FavoriteService {


	// 유저ID에 맞는 즐겨찾기 목록 가져오기
	FavoriteListResponse getFavoritesByUserId(Long userId);



}
