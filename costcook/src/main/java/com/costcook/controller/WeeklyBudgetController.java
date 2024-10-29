package com.costcook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.WeeklyBudgetRequest;
import com.costcook.domain.response.WeeklyBudgetResponse;
import com.costcook.domain.response.WeeklyBudgetUpdateResponse;
import com.costcook.entity.User;
import com.costcook.service.WeeklyBudgetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/budget")
public class WeeklyBudgetController {
	
	private final WeeklyBudgetService budgetService;
	
	// 이번주 예산 설정
	@PostMapping("")
	public ResponseEntity<?> settingWeeklyBudget(@RequestBody WeeklyBudgetRequest budgetRequest ,@AuthenticationPrincipal User user){
		WeeklyBudgetResponse result = budgetService.settingWeeklyBudget(budgetRequest,user);
		return ResponseEntity.status(HttpStatus.CREATED).body(result.getMessage());		
	}
	
	// 이번주 예산 업데이트
	@PatchMapping("")
	public ResponseEntity<?> modifyWeeklyBudget(@RequestBody WeeklyBudgetRequest budgetRequest ,@AuthenticationPrincipal User user){
		WeeklyBudgetUpdateResponse result = budgetService.modifyWeeklyBudget(budgetRequest,user);
		return ResponseEntity.status(HttpStatus.OK).body(result.getMessage());
	}
}
