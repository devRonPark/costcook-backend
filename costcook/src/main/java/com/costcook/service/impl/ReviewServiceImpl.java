package com.costcook.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.costcook.controller.CommonRecipeController;
import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.response.CreateReviewResponse;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.Recipe;
import com.costcook.entity.Review;
import com.costcook.entity.User;
import com.costcook.exceptions.NotFoundException;
import com.costcook.repository.RecipeRepository;
import com.costcook.repository.ReviewRepository;
import com.costcook.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final RecipeRepository recipeRepository;
	private final ReviewRepository reviewRepository;
	
	
	// 리뷰 목록 가져오기
	@Override
	public ReviewListResponse getReviewList(Long recipeId, int page, int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<Review> reviewPage = reviewRepository.findByRecipeId(recipeId, pageable);

		// 응답할 데이터
		return ReviewListResponse.builder()
			.page(page)
			.size(size)
			.totalPages(reviewPage.getTotalPages())
			.totalReviews(reviewPage.getTotalElements())
			.reviews(
				reviewPage.getContent().stream().map(ReviewResponse::toDTO).toList()		
			)
			.build();
	}

	@Override
	public CreateReviewResponse createReview(CreateReviewRequest reviewRequest, User user) {
		Optional<Recipe> optRecipe = recipeRepository.findById(reviewRequest.getRecipeId());
		if (optRecipe.isEmpty()) {
			return CreateReviewResponse.builder().message("해당 레시피를 찾을 수 없습니다.").build();
		}
		Review review = Review.builder()
				.user(user)
				.recipe(optRecipe.get())
				.score(reviewRequest.getScore())
				.comment(reviewRequest.getComment()).build();
		
		Review result = reviewRepository.save(review);
		return CreateReviewResponse.toDTO(result);
	}


	@Override
	public boolean deleteReview(User user, Long reviewId) {
		
		
		// 리뷰를 아이디를 통해서 가져온다
		Optional<Review> optReview = reviewRepository.findById(reviewId);
		if (optReview.isEmpty()) {
			// 해당 리뷰를 찾을 수 없습니다 404 Not Found
			throw new NotFoundException("해당 리뷰를 찾을 수 없습니다.");
		}
		
		// 유저 아이디와 리뷰 아이디의 값이 같은지 비교를 한다 .
		if( !optReview.get().getUser().getId().equals(reviewId)) {
			
			// 리뷰를 삭제할 권한이 없습니다 403 Forbidden
			return false;
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		// 같으면 지운다.
		reviewRepository.deleteById(reviewId);
		return true;
		// 반환 204 No Content

	}


	
	
	
}
