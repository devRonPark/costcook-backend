package com.costcook.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.costcook.domain.request.WeeklyBudgetQueryRequest;
import com.costcook.domain.request.WeeklyBudgetRequest;
import com.costcook.domain.response.WeeklyBudgetResponse;
import com.costcook.domain.response.WeeklyBudgetUpdateResponse;
import com.costcook.entity.Budget;
import com.costcook.entity.User;
import com.costcook.exceptions.NotFoundException;
import com.costcook.repository.UserRepository;
import com.costcook.repository.WeeklyBudgetRepository;
import com.costcook.service.WeeklyBudgetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeeklyBudgetServiceImpl implements WeeklyBudgetService {
	
	private final WeeklyBudgetRepository budgetRepository;
	private final UserRepository userRepository;
	
	@Override
	public WeeklyBudgetResponse settingWeeklyBudget(WeeklyBudgetRequest budgetRequest, User user) {
		Optional<User> optUser = userRepository.findById(user.getId());
		
		if (optUser.isEmpty()) {
			throw new NotFoundException("해당 유저를 찾을 수 없습니다.");
		}
		
		if (budgetRepository.findByUserAndYearAndWeekNumber(optUser.get(), 
		        budgetRequest.getYear(), budgetRequest.getWeekNumber()).isPresent()) {
		    throw new IllegalStateException("해당 연도와 주 번호에 이미 예산이 설정되어 있습니다.");
		}
		
		if (budgetRequest.getWeekNumber() < 1 || budgetRequest.getWeekNumber() > 52 ||budgetRequest.getWeekNumber() == 0 || budgetRequest.getWeeklyBudget() == 0 || budgetRequest.getYear() == 0) {
			throw new IllegalArgumentException("유효한 연도, 주 번호 및 예산을 입력해 주세요.");
		}
		
		Budget budget = Budget.builder()
				.user(optUser.get())
				.year(budgetRequest.getYear())
				.weekNumber(budgetRequest.getWeekNumber())
				.weeklyBudget(budgetRequest.getWeeklyBudget())
				.build();
		
		Budget result = budgetRepository.save(budget);
		return WeeklyBudgetResponse.toDTO(result);
	}

	@Override
	public WeeklyBudgetUpdateResponse modifyWeeklyBudget(WeeklyBudgetRequest budgetRequest, User user) {
		Optional<User> optUser = userRepository.findById(user.getId());
		
		if (optUser.isEmpty()) {
			throw new NotFoundException("해당 유저를 찾을 수 없습니다.");
		}
		
		if (budgetRequest.getWeekNumber() < 1 || budgetRequest.getWeekNumber() > 52 ||budgetRequest.getWeekNumber() == 0 || budgetRequest.getWeeklyBudget() == 0 || budgetRequest.getYear() == 0) {
			throw new IllegalArgumentException("유효한 연도, 주 번호 및 예산을 입력해 주세요.");
		}
		
		Optional<Budget> weeklyBudget = budgetRepository.findByUserAndYearAndWeekNumber(optUser.get(), 
		        budgetRequest.getYear(), budgetRequest.getWeekNumber());
		
		if (!weeklyBudget.isPresent()) {
		    throw new NotFoundException("해당 주의 예산이 존재하지 않습니다.");
		}
		
		weeklyBudget.get().setYear(budgetRequest.getYear());
		weeklyBudget.get().setWeekNumber(budgetRequest.getWeekNumber());
		weeklyBudget.get().setWeeklyBudget(budgetRequest.getWeeklyBudget());
		Budget result = budgetRepository.save(weeklyBudget.get());
		return WeeklyBudgetUpdateResponse.toDTO(result);
	}

	@Override
	public WeeklyBudgetResponse getWeeklyBudget(WeeklyBudgetQueryRequest weeklyBudgetQueryRequest, User user) {
	    Optional<User> optUser = userRepository.findById(user.getId());

	    if (optUser.isEmpty()) {
	        throw new NotFoundException("해당 유저를 찾을 수 없습니다.");
	    }

	    Optional<Budget> thisWeekBudget = budgetRepository.findByUserAndYearAndWeekNumber(
	            user, 
	            weeklyBudgetQueryRequest.getYear(), 
	            weeklyBudgetQueryRequest.getWeekNumber()
	    );

	    return WeeklyBudgetResponse.builder()
	    	.budget(thisWeekBudget.map(Budget::getWeeklyBudget).orElse(10000))
	    	.message(thisWeekBudget.isPresent() ? "예산 가져오기 성공" : "기본값 설정")
	    	.year(weeklyBudgetQueryRequest.getYear())
	    	.weekNumber(weeklyBudgetQueryRequest.getWeekNumber())
	    	.build();
	}

}
