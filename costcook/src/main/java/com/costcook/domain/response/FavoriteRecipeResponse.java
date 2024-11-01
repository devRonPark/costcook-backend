package com.costcook.domain.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteRecipeResponse {
    private Long id; // 레시피 ID
    private String title; // 레시피 제목
    private int price; // 레시피 가격
    private double avgRatings; // 평균 평점
    private String thumbnailUrl; // 썸네일 URL
}
