package com.costcook.domain.response;

import com.costcook.entity.Review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateReviewResponse {

	private Long reviewId;
	private String message;

	// Recipe -> response 변환
	public static UpdateReviewResponse toDTO(Review review) {
		return UpdateReviewResponse.builder()
				.reviewId(review.getId())
				.message("리뷰가 수정되었습니다.")
				.build();
	}
	
}