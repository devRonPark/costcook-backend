package com.costcook.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.response.CreateReviewResponse;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.User;

public interface ReviewService {

	CreateReviewResponse createReview(CreateReviewRequest reviewRequest, User user);

	boolean deleteReview(User user, Long reviewId);

	// 리뷰 목록 불러오기
	ReviewListResponse getReviewList(Long recipeId, int page, int size);
}
