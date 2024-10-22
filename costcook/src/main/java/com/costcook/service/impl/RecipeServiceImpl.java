package com.costcook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.costcook.domain.response.RecipeResponse;
import com.costcook.entity.RecipeItem;
import com.costcook.repository.RecipeRepository;
import com.costcook.service.RecipeService;
import com.costcook.util.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
	
	private final RecipeRepository recipeRepository;
	private final FileUtils fileUtils;

	
	// 레시피 목록 조회
	@Override
	public List<RecipeResponse> getRecipes(int page, int size, String sort, String order) {
		Pageable pageable = PageRequest.of(page, size);
		Page<RecipeItem> recipePage;
		
		// 정렬
        if (sort.equals("viewCount")) {
        	if (order.equals("asc")) { // 오름차순
        		recipePage = recipeRepository.findAllByOrderByViewCountAsc(pageable);
        	} else { // 내림차순
        		recipePage = recipeRepository.findAllByOrderByViewCountDesc(pageable);
        	}
        } else if (sort.equals("avgRatings")) {
        	if (order.equals("asc")) { // 오름차순
        		recipePage = recipeRepository.findAllByOrderByAvgRatingsAsc(pageable);
        	} else { // 내림차순
        		recipePage = recipeRepository.findAllByOrderByAvgRatingsDesc(pageable);        		
        	}
        } else { // 생성일(디폴트)
        	if (order.equals("asc")) { // 오름차순
        		recipePage = recipeRepository.findAllByOrderByCreatedAtAsc(pageable);
        	} else {
        		recipePage = recipeRepository.findAllByOrderByCreatedAtDesc(pageable);        		
        	}
        }
		return recipePage.getContent().stream().map(RecipeResponse::toDTO).toList();
	}
	
	
	// 전체 레시피 수 조회 : 총 페이지를 미리 입력하여, 무한 로딩 방지
	@Override
	public long getTotalRecipes() {
		return recipeRepository.count();
	}

	
	// 레시피 상세 조회
	@Override
	public RecipeResponse getRecipeById(Long id) {
		RecipeItem product = recipeRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("제품 정보가 없습니다."));
		RecipeResponse productResponse = RecipeResponse.toDTO(product);
		return productResponse;
	}
	
	

	

}
