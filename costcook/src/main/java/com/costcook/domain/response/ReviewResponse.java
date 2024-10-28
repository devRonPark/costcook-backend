package com.costcook.domain.response;

import java.time.LocalDateTime;

import com.costcook.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReviewResponse {

	private String nickname, profileUrl;
	private int score;
	private String comment;
	private boolean status;
	private LocalDateTime createdAt;

	// Recipe -> response 변환
	public static ReviewResponse toDTO(Review review) {
		System.out.println(review);
		return ReviewResponse.builder()
				.nickname(review.getUser().getNickname())
				.profileUrl(review.getUser().getProfileUrl())
				.score(review.getScore())
				.comment(review.getComment())
				.status(review.isStatus())
				.createdAt(null)
				.build();
	}
	
}