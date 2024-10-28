package com.costcook.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.CreateReviewResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.domain.response.UpdateReviewResponse;
import com.costcook.entity.Recipe;
import com.costcook.entity.Review;
import com.costcook.entity.User;
import com.costcook.exceptions.ErrorResponse;
import com.costcook.exceptions.MessageExcepiton;
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
		return reviewList.stream().map(review -> ReviewResponse.toDTO(review)).toList();
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
		if( !optReview.get().getUser().getId().equals(user.getId())) {
			
			// 리뷰를 삭제할 권한이 없습니다 403 Forbidden
			return false;
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		
		// 같으면 지운다.
		reviewRepository.deleteById(reviewId);
		return true;
		// 반환 204 No Content

	}

	@Override
	public ReviewResponse modifyReview(UpdateReviewRequest updateReviewRequest, User user, Long reviewId) {
		
		Optional<Review> optReview = reviewRepository.findById(reviewId);
		if (optReview.isEmpty()) {
			// 해당 리뷰를 찾을 수 없습니다 404 Not Found
			throw new NotFoundException("해당 리뷰를 찾을 수 없습니다.");
		}
		
		// 유저 아이디와 리뷰 아이디의 값이 같은지 비교를 한다 .
		if( !optReview.get().getUser().getId().equals(user.getId())) {
			
			throw new IllegalArgumentException("리뷰를 수정할 권한이 없습니다.");
		}
		
		
		  // 요청 데이터 유효성 검사
		if ( updateReviewRequest.getScore() < 1 || updateReviewRequest.getScore() > 5  || updateReviewRequest.getComment().trim().isEmpty()) {
			
			
			throw new IllegalArgumentException("요청 데이터가 유효하지 않습니다. 평점은 1에서 5 사이여야 하며, 댓글은 비워둘 수 없습니다.");
			
		}
		Review reviewToUpdate  = optReview.get();
		reviewToUpdate .setScore(updateReviewRequest.getScore());
		reviewToUpdate .setComment(updateReviewRequest.getComment());
		
		Review updatedReview = reviewRepository.save(reviewToUpdate);
		
		return ReviewResponse.toDTO(updatedReview);
	}
	
	
}
