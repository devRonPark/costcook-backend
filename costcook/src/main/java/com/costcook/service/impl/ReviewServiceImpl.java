package com.costcook.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.CreateReviewResponse;
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

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final RecipeRepository recipeRepository;
	private final ReviewRepository reviewRepository;

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
	public List<ReviewResponse> getReviewList(Long id) {
		List<Review> reviewList = reviewRepository.findAllByRecipeId(id);
		return reviewList.stream()
		        .filter(review -> review.getDeletedAt() == null) // 삭제되지 않은 리뷰만 필터링
		        .map(ReviewResponse::toDTO)
		        .toList();
		}

	
	
	// 삭제

	@Override
	public boolean deleteReview(User user, Long reviewId) {
		
		
		// 리뷰를 아이디를 통해서 가져온다
		Optional<Review> optReview = reviewRepository.findById(reviewId);
		if (optReview.isEmpty() || optReview.get().getDeletedAt() != null) {
			// 해당 리뷰를 찾을 수 없습니다 404 Not Found
			throw new NotFoundException("해당 리뷰를 찾을 수 없습니다.");
		}
		
		// 유저 아이디와 리뷰 작성자의 아이디의 값이 같은지 비교를 한다 .
		if( !optReview.get().getUser().getId().equals(user.getId())) {
			
			
			// 403 Forbidden
			throw new ForbiddenException("리뷰를 수정할 권한이 없습니다.");
		}
		
		
		// 같으면 지운다.
		reviewRepository.deleteById(reviewId);
		// 반환 204 No Content
		return true;

	}
	
	// 수정


	@Override
	public ReviewResponse modifyReview(UpdateReviewRequest updateReviewRequest, User user, Long reviewId) {
		
		
		
		Optional<Review> optReview = reviewRepository.findByRecipeIdAndUserId(reviewId, user.getId());
		System.out.println(optReview);
		if (optReview.isEmpty()) {
			// 해당 리뷰를 찾을 수 없습니다 404 Not Found
			throw new NotFoundException("해당 리뷰를 찾을 수 없습니다.");
		}
		
		Review reviewToUpdate  = optReview.get();
		
		// 현재 로그인한 유저가 해당 리뷰 작성자인지 확인
		if( !reviewToUpdate.getUser().getId().equals(user.getId())) {
			// 권한이 없을 때 403 Forbidden
			throw new ForbiddenException("리뷰를 수정할 권한이 없습니다.");
		}
		  // 요청 데이터 유효성 검사
		if ( updateReviewRequest.getScore() < 1 || updateReviewRequest.getScore() > 5  || updateReviewRequest.getComment().trim().isEmpty()) {
			
			
			throw new IllegalArgumentException("요청 데이터가 유효하지 않습니다. 평점은 1에서 5 사이여야 하며, 댓글은 비워둘 수 없습니다.");
			
		}
		
		// 변경 사항 반영
		reviewToUpdate.setScore(updateReviewRequest.getScore());
		reviewToUpdate.setComment(updateReviewRequest.getComment());
		
		// updated_at 필드를 현재 시간으로 설정
		reviewToUpdate.setUpdatedAt(java.time.LocalDateTime.now());
		
		 // 변경 사항 저장
		Review updatedReview = reviewRepository.save(reviewToUpdate);
		
		 // 수정된 리뷰 정보를 반환
		return ReviewResponse.toDTO(updatedReview);
	}
	
	
}
