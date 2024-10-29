package com.costcook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.costcook.entity.Budget;
import com.costcook.entity.User;

public interface WeeklyBudgetRepository extends JpaRepository<Budget, Long>{
	
	Optional<Budget> findByUserAndYearAndWeekNumber(User user, int year, int weekNumber);

}
