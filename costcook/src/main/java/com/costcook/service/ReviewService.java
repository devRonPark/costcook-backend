package com.costcook.service;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.User;

public interface ReviewService {

	ReviewResponse createReview(CreateReviewRequest reviewRequest, User user);

	boolean deleteReview(User user, Long reviewId);

	// 리뷰 수정
	ReviewResponse modifyReview(UpdateReviewRequest updateReviewRequest, User user, Long reviewId);

	// 리뷰 목록 가져오기(모든 사용자)
	ReviewListResponse getReviewList(Long recipeId, int page, int size);

	// 리뷰 목록 가져오기(마이페이지)
	ReviewListResponse getReviewListByUserWithPagination(User user, int page);

	ReviewResponse getReviewByUserAndRecipe(User user, Long recipeId);

}
