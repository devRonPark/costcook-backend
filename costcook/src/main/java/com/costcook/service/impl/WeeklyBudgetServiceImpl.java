package com.costcook.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.costcook.domain.ReviewStatsDTO;
import com.costcook.domain.request.WeeklyBudgetQueryRequest;
import com.costcook.domain.request.WeeklyBudgetRequest;
import com.costcook.domain.response.RecipeResponse;
import com.costcook.domain.response.WeeklyBudgetResponse;
import com.costcook.domain.response.WeeklyBudgetUpdateResponse;
import com.costcook.domain.response.WeeklyUsedBudgetResponse;
import com.costcook.entity.Budget;
import com.costcook.entity.Recipe;
import com.costcook.entity.User;
import com.costcook.exceptions.NotFoundException;
import com.costcook.repository.FavoriteRepository;
import com.costcook.repository.RecipeRepository;
import com.costcook.repository.RecommendedRecipeRepository;
import com.costcook.repository.UserRepository;
import com.costcook.repository.WeeklyBudgetRepository;
import com.costcook.service.WeeklyBudgetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeeklyBudgetServiceImpl implements WeeklyBudgetService {
	
	private final WeeklyBudgetRepository budgetRepository;
	private final UserRepository userRepository;
	private final RecommendedRecipeRepository recommendedRecipeRepository;
	private final RecipeRepository recipeRepository;
	private final RecipeServiceImpl recipeServiceImpl;

	
	// 예산 설정
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

	// 예산 수정
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

	// 예산 조회
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

	// 주간 사용 예산, 사용 레시피 상세정보
	@Override
	public WeeklyUsedBudgetResponse getWeeklyUsedBudget(WeeklyBudgetQueryRequest weeklyBudgetQueryRequest, User user) {
	    Optional<User> optUser = userRepository.findById(user.getId());
	    // 유저 정보 가져오기
	    if (optUser.isEmpty()) {
	        throw new NotFoundException("해당 유저를 찾을 수 없습니다.");
	    }
	    // 예산 설정 연,월 가져오기
	    Optional<Budget> thisWeekBudget = budgetRepository.findByUserAndYearAndWeekNumber(
	            user, 
	            weeklyBudgetQueryRequest.getYear(), 
	            weeklyBudgetQueryRequest.getWeekNumber()
	    );
	    // 사용한 레시피 ID 조회
	    List<Long> usedRecipeId = recommendedRecipeRepository.findRecipeIdsByUserAndYearAndWeekNumberAndIsUsed(
	            user, 
	            weeklyBudgetQueryRequest.getYear(), 
	            weeklyBudgetQueryRequest.getWeekNumber(),
	            true // isUsed가 true(1)인 레시피만 조회
	    );

	    // 사용한 레시피 상세 정보 가져오기(레시피 상세정보 메소드)    
        List<RecipeResponse> recipeResponses = new ArrayList<>();
        for (Long recipeId : usedRecipeId) {
            RecipeResponse recipeResponse = recipeServiceImpl.getRecipeById(recipeId, user);
            recipeResponses.add(recipeResponse);
        }
	    
	    // 이번 주 예산(사용자가 설정한 예산가져오기(디폴트 1만원)
	    int weeklyBudget = thisWeekBudget.map(Budget::getWeeklyBudget).orElse(10000);
	    // 사용한 레시피 가격 총합(사용 레시피 ID를 조회하여 불러오기)
	    Long LongWeeklyUsedPrice = recipeRepository.findTotalPriceByRecipeId(usedRecipeId);
	    int weeklyUsedPrice = LongWeeklyUsedPrice != null ? LongWeeklyUsedPrice.intValue() : 0;
	    // 남은 예산 (이번 주 예산 - 사용 레시피 가격 총합)
	    int remainingBudget = weeklyBudget - weeklyUsedPrice;

	    // 응답 객체 생성
	    return WeeklyUsedBudgetResponse.builder()
	    		.userId(user.getId())
	            .weeklyBudget(weeklyBudget) // 이번 주 예산
	            .usedBudget(weeklyUsedPrice) // 총 사용 가격
	            .remainingBudget(remainingBudget) // 남은 예산
	            .year(weeklyBudgetQueryRequest.getYear())
	            .weekNumber(weeklyBudgetQueryRequest.getWeekNumber())
	            .recipes(recipeResponses) // 레시피 상세정보
	            .build();
		}
	
	
}
