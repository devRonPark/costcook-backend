package com.costcook.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "budgets")
public class Budget {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "user_id", updatable = false) // user_id는 변경 불가
    private User user;
	
	// 예산이 설정된 년도
	@Column(nullable = false)
	private int year;
	
	// 예산이 설정된 주 번호
	@Column(name = "week_number", nullable = false)
	private int weekNumber;
	
	// 주간 예산 금액
	@Column(name = "weekly_budget", nullable = false)
	private int weeklyBudget;
	
	// 등록일
	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	// 수정일
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	

}
