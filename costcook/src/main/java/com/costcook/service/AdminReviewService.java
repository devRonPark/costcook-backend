package com.costcook.service;

import com.costcook.domain.response.ReviewListResponse;

public interface AdminReviewService {

  ReviewListResponse getReviewList(int page, int size, String sortBy, String direction);
  
}
