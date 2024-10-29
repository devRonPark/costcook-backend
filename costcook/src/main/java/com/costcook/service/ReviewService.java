package com.costcook.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.costcook.domain.request.CreateReviewRequest;
import com.costcook.domain.request.UpdateReviewRequest;
import com.costcook.domain.response.CreateReviewResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.domain.response.UpdateReviewResponse;
import com.costcook.entity.User;

public interface ReviewService {

	CreateReviewResponse createReview(CreateReviewRequest reviewRequest, User user);

	List<ReviewResponse> getReviewList(Long id);

	boolean deleteReview(User user, Long reviewId);

	ReviewResponse modifyReview(UpdateReviewRequest updateReviewRequest, User user, Long reviewId);

}
