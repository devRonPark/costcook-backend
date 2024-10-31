package com.costcook.domain.response;

import com.costcook.entity.Budget;

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
public class WeeklyBudgetUpdateResponse {

    private String message;         // 응답 메시지
    private int year;              // 등록, 수정된 예산의 연도
    private int weekNumber;        // 등록, 수정된 예산의 주 번호
    private int budget;     // 등록, 수정된 이번 주 식비 예산 (원 단위)
    
    public static WeeklyBudgetUpdateResponse toDTO(Budget budget) {
    	return WeeklyBudgetUpdateResponse.builder()
    			.message("이번 주 예산이 수정되었습니다.")
    			.year(budget.getYear())
    			.weekNumber(budget.getWeekNumber())
    			.budget(budget.getWeeklyBudget())
    			.build();
    }
    
}