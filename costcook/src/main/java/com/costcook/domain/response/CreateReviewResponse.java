package com.costcook.domain.response;

import com.costcook.entity.Review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReviewResponse {

	private Long reviewId;
	private String message;

	// Recipe -> response 변환
	public static CreateReviewResponse toDTO(Review review) {
		return CreateReviewResponse.builder()
				.reviewId(review.getId())
				.message("리뷰가 작성되었습니다.")
				.build();
	}
	
}