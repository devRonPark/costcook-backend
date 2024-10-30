package com.costcook.service;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.CreateReviewResponse;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.User;

public interface ReviewService {

//	// 리뷰 작성
//	CreateReviewResponse createReview(CreateReviewRequest reviewRequest, User user);
	
	////////////
	// 리뷰 작성 TEST
	CreateReviewResponse createReview(CreateReviewRequest reviewRequest);
////////////////
	
	
	// 리뷰 삭제
	boolean deleteReview(User user, Long reviewId);

	// 리뷰 수정
	ReviewResponse modifyReview(UpdateReviewRequest updateReviewRequest, User user, Long reviewId);

	// 리뷰 목록 가져오기(모든 사용자)
	ReviewListResponse getReviewList(Long recipeId, int page, int size);

	// 리뷰 목록 가져오기(마이페이지)
	ReviewListResponse getReviewListByUserWithPagination(User user, int page);

}
