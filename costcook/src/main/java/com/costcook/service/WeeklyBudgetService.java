package com.costcook.service;

import com.costcook.domain.request.WeeklyBudgetQueryRequest;
import com.costcook.domain.request.WeeklyBudgetRequest;
import com.costcook.domain.response.WeeklyBudgetResponse;
import com.costcook.domain.response.WeeklyBudgetUpdateResponse;
import com.costcook.domain.response.WeeklyUsedBudgetResponse;
import com.costcook.entity.User;

public interface WeeklyBudgetService {

	WeeklyBudgetResponse settingWeeklyBudget(WeeklyBudgetRequest budgetRequest, User user);

	WeeklyBudgetUpdateResponse modifyWeeklyBudget(WeeklyBudgetRequest budgetRequest, User user);

	WeeklyBudgetResponse getWeeklyBudget(WeeklyBudgetQueryRequest weeklyBudgetQueryRequest, User user);

	// 주간 사용 예산
	WeeklyUsedBudgetResponse getWeeklyUsedBudget(WeeklyBudgetQueryRequest weeklyBudgetQueryRequest, User user);

}
