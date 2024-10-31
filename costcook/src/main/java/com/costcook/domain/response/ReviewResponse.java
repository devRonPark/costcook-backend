package com.costcook.domain.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.costcook.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {		
	private Long id;
	private int score;
	private String comment;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createdAt;
	private User user;
	private Recipe recipe;
	
	@Data
	@Builder
    @AllArgsConstructor
    @NoArgsConstructor
	public static class User{
		private Long id;
		private String nickname;
		private String profileUrl;
	}

	@Data
	@Builder
    @AllArgsConstructor
    @NoArgsConstructor
	public static class Recipe{
		private Long id;
		private String title;
		private String thumbnailUrl;
	}
	

	// Review -> response 변환
	public static ReviewResponse toDTO(Review review) {
		return ReviewResponse.builder()
				.id(review.getId())
				.score(review.getScore())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .user(User.builder()
                	.id(review.getUser().getId())
                    .nickname(review.getUser().getNickname())
                    .profileUrl(review.getUser().getProfileUrl())
                    .build())
                .recipe(Recipe.builder()
                    .id(review.getRecipe().getId())
					.title(review.getRecipe().getTitle())
                    .thumbnailUrl(review.getRecipe().getThumbnailUrl())
                    .build())
                .build();						
	}
	
}