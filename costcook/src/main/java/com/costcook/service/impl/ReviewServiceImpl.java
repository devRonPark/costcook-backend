package com.costcook.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	// 레시피 상세페이지 > 리뷰 목록 가져오기
	@Override
	public ReviewListResponse getReviewList(Long recipeId, int page, int size) {
		int validPage = Math.max(page, 1) - 1; // 최소 페이지 설정: 1부터
		Pageable pageable = PageRequest.of(validPage, size);
		// 생성일 기준으로 정렬된 리뷰 목록을 가져옴
		// 조회 시 where 조건: deletedAt 이 null 인 경우(삭제되지 않은 경우) && status 가 false 인 경우(비공개 처리가 되지 않은 경우)
		Page<Review> reviewPage = reviewRepository.findByRecipeIdOrderByCreatedAtDesc(recipeId, pageable);
		
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
//	public CreateReviewResponse createReview(CreateReviewRequest reviewRequest, User user) {
	
	// 리뷰 등록 TEST(로그인 상태라 가정)
	
	public CreateReviewResponse createReview(CreateReviewRequest reviewRequest) {
		Optional<Recipe> optRecipe = recipeRepository.findById(reviewRequest.getRecipeId());
		if (optRecipe.isEmpty()) {
			throw new NotFoundException("해당 레시피를 찾을 수 없습니다.");
		}
		Review review = Review.builder()
//			.user(user)
			.recipe(optRecipe.get())
			.score(reviewRequest.getScore())
			.comment(reviewRequest.getComment())
			.build();
		
		Review result = reviewRepository.save(review);
		return CreateReviewResponse.toDTO(result);
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

	@Override
	public ReviewListResponse getReviewListByUserWithPagination(User user, int page) {
		int size = 9; // 기본 페이지 크기 설정
		int validPage = Math.max(page, 1) - 1; // 최소 1 이상의 값을 보장

		// 페이지네이션 설정
		Pageable pageable = PageRequest.of(validPage, size);
		// 조회 시 where 조건: deletedAt 이 null 인 경우(삭제되지 않은 경우) && status 가 false 인 경우(비공개 처리가 되지 않은 경우)
		Page<Review> reviewPage = reviewRepository.findAllByUserIdAndStatusFalseAndNotDeleted(user.getId(), pageable);

		// 빌더 패턴을 사용하여 응답 구성
		return ReviewListResponse.builder()
			.page(page)
			.size(reviewPage.getNumberOfElements()) // 현재 페이지의 리뷰 개수 
			.totalPages(reviewPage.getTotalPages())
			.totalReviews(reviewPage.getTotalElements())
			.reviews(
				reviewPage.getContent().stream() // Page에서 List로 변환 후 스트림 처리
					.map(review -> ReviewResponse.toDTO(review)) // 각 리뷰를 DTO로 변환
					.collect(Collectors.toList()) // 변환 결과를 리스트로 수집
			)
			.build();
	}
}
