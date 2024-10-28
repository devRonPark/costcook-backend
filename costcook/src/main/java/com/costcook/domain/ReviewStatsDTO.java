package com.costcook.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewStatsDTO {
	
	private final Long reviewCount;
	private final Double averageScore;
	
}
