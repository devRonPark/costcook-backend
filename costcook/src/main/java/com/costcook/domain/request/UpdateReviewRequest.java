package com.costcook.domain.request;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
public class UpdateReviewRequest {
	
	private Long reviewId;
	private int score;
	private String comment;
	
	
	//수정
	
	
}