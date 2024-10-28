package com.costcook.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.CreateReviewResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.User;
import com.costcook.exceptions.NotFoundException;
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
	public ResponseEntity<CreateReviewResponse> createReview(@RequestBody CreateReviewRequest reviewRequest, @AuthenticationPrincipal User user) {
		CreateReviewResponse result = reviewService.createReview(reviewRequest, user);
		if (result.getReviewId() == null) 
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}
	

	
	// 리뷰 삭제
	@DeleteMapping("/{reviewId}")
	public ResponseEntity<?> deleteReview(@AuthenticationPrincipal User user, @PathVariable("reviewId") Long id){
		boolean isReviewDeleted = reviewService.deleteReview(user, id);

		if (!isReviewDeleted) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();				
	}
	
	
	// 리뷰 수정
	@PatchMapping("/{reviewId}")
	public ResponseEntity<?> updateReview(@RequestBody UpdateReviewRequest updateReviewRequest, @AuthenticationPrincipal User user, @PathVariable("reviewId") Long id ){
			ReviewResponse result = reviewService.modifyReview(updateReviewRequest, user, id);
			
			return ResponseEntity.ok("리뷰가 수정되었습니다.");
	}
	
	
}