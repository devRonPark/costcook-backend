package com.costcook.service.impl;

import org.springframework.stereotype.Service;

import com.costcook.domain.response.FavoriteListResponse;
import com.costcook.repository.FavoriteRepository;
import com.costcook.service.FavoriteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

	private final FavoriteRepository favoriteRepository;
	
	// 유저ID로 유저가 작성한 즐겨찾기 목록 가져오기
    public FavoriteListResponse getFavoritesByUserId(Long userId) {
    	
    	log.info("즐겨찾기 목록 가져오기{}: " );
		return null;

    	
    }


	
	
	

}
