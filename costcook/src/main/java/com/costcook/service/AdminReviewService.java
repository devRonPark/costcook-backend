package com.costcook.service;

import java.util.Map;

import com.costcook.domain.response.ReviewListResponse;

public interface AdminReviewService {

  ReviewListResponse getReviewList(Map<String, String> params);

  boolean updateReviewStatus(Long reviewId);
  
}
