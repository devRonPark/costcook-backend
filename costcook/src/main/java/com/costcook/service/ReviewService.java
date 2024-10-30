package com.costcook.service;

import java.util.List;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.CreateReviewResponse;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.User;

public interface ReviewService {

	CreateReviewResponse createReview(CreateReviewRequest reviewRequest, User user);

	boolean deleteReview(User user, Long reviewId);

	ReviewResponse modifyReview(UpdateReviewRequest updateReviewRequest, User user, Long reviewId);

	ReviewListResponse getReviewList(Long recipeId, int page, int size);

	List<ReviewResponse> getReviewList(Long recipeId);

	ReviewListResponse getReviewListByUserWithPagination(User user, int page);

}
