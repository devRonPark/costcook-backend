package com.costcook.domain.response;

import com.costcook.entity.Recipe;
import com.costcook.entity.User;

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
public class RecommendedRecipeResponse {

    private int year;              // 등록된 예산의 연도
    private int weekNumber;        // 등록된 예산의 주 번호
    private boolean isUsed;        // 사용 여부
    private Recipe recipe;         // 추천된 레시피
    private User user;             // 사용자 정보
    
}