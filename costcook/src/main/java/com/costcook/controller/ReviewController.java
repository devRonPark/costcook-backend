package com.costcook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.User;
import com.costcook.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;
	
	// 리뷰 작성
	@PostMapping("")
	public ResponseEntity<ReviewResponse> createReview(@RequestBody CreateReviewRequest reviewRequest, @AuthenticationPrincipal User user) {
		ReviewResponse result = reviewService.createReview(reviewRequest, user);
		if (result.getId() == null) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}
	
	// 리뷰 삭제
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<?> deleteReview(@AuthenticationPrincipal User user, @PathVariable("reviewId") Long reviewId) {
		reviewService.deleteReview(user, reviewId);
		return ResponseEntity.noContent().build();
    }
	
	// 리뷰 수정
	@PatchMapping("/{reviewId}")
	public ResponseEntity<?> updateReview(@RequestBody UpdateReviewRequest updateReviewRequest, @AuthenticationPrincipal User user, @PathVariable("reviewId") Long reviewId ){
			ReviewResponse result = reviewService.modifyReview(updateReviewRequest, user, reviewId);
			return ResponseEntity.ok(result);
	}
	
	
}