package com.costcook.service;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.User;

public interface ReviewService {

	ReviewResponse createReview(CreateReviewRequest reviewRequest, User user);

	boolean deleteReview(User user, Long reviewId);

	ReviewResponse modifyReview(UpdateReviewRequest updateReviewRequest, User user, Long reviewId);

	ReviewListResponse getReviewList(Long recipeId, int page, int size);

	ReviewListResponse getReviewListByUserWithPagination(User user, int page);

}
