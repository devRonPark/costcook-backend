package com.costcook.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyBudgetQueryRequest {

    private int year;               // 등록, 수정할 예산의 연도
    private int weekNumber;         // 등록, 수정할 예산의 주 번호 (1~52)
}