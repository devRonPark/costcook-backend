
package com.costcook.domain.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyRecipesResponse {
    private int year;                        // 해당 연도
    private int weekNumber;                  // 해당 주 번호
    
    private List<Recipe> recipes; // 추천 레시피 목록

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recipe {
        private Long id;                     // 레시피 식별자
        private String title;                // 레시피 제목
        private String thumbnailUrl;         // 레시피 썸네일 URL
        private int price;                   // 가격
        private int favoriteCount;           // 북마크 수
        private double avgRatings;           // 평균 평점
        private int servings;
        private boolean isUsed;
    }
}
