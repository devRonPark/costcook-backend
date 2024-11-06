package com.costcook.domain.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpOrLoginRequest {

    // 소셜 로그인 키 (Google, Kakao 등의 고유 식별자)
    private String socialKey;

    // 이메일 주소
    private String email;

    // 제공자 (소셜 로그인 제공자, 예: 'KAKAO', 'GOOGLE')
    private String provider;

    // (비회원) 즐겨찾기 추가된 레시피 ID 목록
    private List<Long> favoriteRecipeIds;

    // (비회원) 설정한 예산 정보
    private WeeklyBudgetRequest budget;

    // (비회원) 추가한 레시피 목록
    private List<RecommendedRecipeRequest> recommendedRecipes;
}

