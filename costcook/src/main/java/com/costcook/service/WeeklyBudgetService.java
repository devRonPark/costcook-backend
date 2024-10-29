package com.costcook.service;

import com.costcook.domain.request.WeeklyBudgetRequest;
import com.costcook.domain.response.WeeklyBudgetResponse;
import com.costcook.domain.response.WeeklyBudgetUpdateResponse;
import com.costcook.entity.User;

public interface WeeklyBudgetService {

	WeeklyBudgetResponse settingWeeklyBudget(WeeklyBudgetRequest budgetRequest, User user);

	WeeklyBudgetUpdateResponse modifyWeeklyBudget(WeeklyBudgetRequest budgetRequest, User user);

}
