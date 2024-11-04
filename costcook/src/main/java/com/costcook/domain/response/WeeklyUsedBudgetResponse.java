package com.costcook.domain.response;

import java.util.List;

import com.costcook.entity.Recipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 주간 예산 사용 DTO
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyUsedBudgetResponse {
	
	private int year;
	private int weekNumber;
	private int weeklyBudget; // 이번 주 예산
	private int usedBudget; // 사용한 예산
	private int remainingBudget; // 남은 예산
	private List<RecipeResponse> recipes; // 사용 레시피 정보


}
