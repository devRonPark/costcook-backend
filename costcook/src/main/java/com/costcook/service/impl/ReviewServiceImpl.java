package com.costcook.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.costcook.controller.CommonRecipeController;
import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.CreateReviewResponse;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.Recipe;
import com.costcook.entity.Review;
import com.costcook.entity.User;
import com.costcook.exceptions.ForbiddenException;
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

//		log.info("리뷰정보: {}", reviewPage.getContent());
//		log.info("리뷰정보: {}", reviewPage.getNumber());
//		log.info("리뷰정보: {}", reviewPage.getTotalPages());
//		log.info("리뷰정보: {}", reviewPage.getSort());
//		log.info("리뷰정보: {}", reviewPage.getPageable());
		
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

	
	// 리뷰 등록
	@Override
	public CreateReviewResponse createReview(CreateReviewRequest reviewRequest, User user) {
		Optional<Recipe> optRecipe = recipeRepository.findById(reviewRequest.getRecipeId());
		if (optRecipe.isEmpty()) {
			throw new NotFoundException("해당 레시피를 찾을 수 없습니다.");
		}
		Review review = Review.builder()
			.user(user)
			.recipe(optRecipe.get())
			.score(reviewRequest.getScore())
			.comment(reviewRequest.getComment())
			.build();
		
		Review result = reviewRepository.save(review);
		return CreateReviewResponse.toDTO(result);
	}

	@Override
	public List<ReviewResponse> getReviewList(Long recipeId) {
		List<Review> reviewList = reviewRepository.findAllByRecipeId(recipeId);
		return reviewList.stream()
	        .filter(review -> review.getDeletedAt() == null) // 삭제되지 않은 리뷰만 필터링
	        .map(ReviewResponse::toDTO)
	        .toList();
		}
	
	// 삭제
	@Transactional
	@Override
	public boolean deleteReview(User user, Long reviewId) {
		// 리뷰를 아이디를 통해서 가져온다
		Optional<Review> optReview = reviewRepository.findById(reviewId);
		System.out.println(optReview);
		
		
		if (optReview.isEmpty() || optReview.get().getDeletedAt() != null) {
			// 해당 리뷰를 찾을 수 없습니다 404 Not Found
			throw new NotFoundException("해당 리뷰를 찾을 수 없습니다.");
		}

		Review review = optReview.get();
		System.out.println(review.getUser().getId());
		System.out.println(user.getId());
		// 유저 아이디와 리뷰 작성자의 아이디의 값이 같은지 비교를 한다 .
		if (!review.getUser().getId().equals(user.getId())) {
			// 403 Forbidden
			throw new ForbiddenException("리뷰를 수정할 권한이 없습니다.");
		}
		
		// 같으면 지운다.
		LocalDateTime now = LocalDateTime.now();
        reviewRepository.softDelete(reviewId, now);
        
		// 반환 204 No Content
		return true;
	}
	
	// 수정
	@Override
	public ReviewResponse modifyReview(UpdateReviewRequest updateReviewRequest, User user, Long reviewId) {
		Optional<Review> optReview = reviewRepository.findById(reviewId);
		if (optReview.isEmpty() || optReview.get().getDeletedAt() != null) {
			// 해당 리뷰를 찾을 수 없습니다 404 Not Found
			throw new NotFoundException("해당 리뷰를 찾을 수 없습니다.");
		}
		
		Review reviewToUpdate  = optReview.get();
		
		// 현재 로그인한 유저가 해당 리뷰 작성자인지 확인
		if(!reviewToUpdate.getUser().getId().equals(user.getId())) {
			// 권한이 없을 때 403 Forbidden
			throw new ForbiddenException("리뷰를 수정할 권한이 없습니다.");
		}
		  // 요청 데이터 유효성 검사
		if (updateReviewRequest.getScore() < 1 || updateReviewRequest.getScore() > 5  || updateReviewRequest.getComment().trim().isEmpty()) {
			throw new IllegalArgumentException("요청 데이터가 유효하지 않습니다. 평점은 1에서 5 사이여야 하며, 댓글은 비워둘 수 없습니다.");
		}
		
		// 변경 사항 반영
		reviewToUpdate.setScore(updateReviewRequest.getScore());
		reviewToUpdate.setComment(updateReviewRequest.getComment());
		
		 // 변경 사항 저장
		Review updatedReview = reviewRepository.save(reviewToUpdate);
		
		 // 수정된 리뷰 정보를 반환
		return ReviewResponse.toDTO(updatedReview);
	}
}
