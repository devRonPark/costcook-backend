package com.costcook.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedRecipeRequest {

	private int year; // 등록, 수정할 예산의 연도
	private int weekNumber; // 등록, 수정할 예산의 주 번호 (1~52)
	private boolean isUsed;
	private Long recipeId;

}