package com.costcook.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeListResponse {
    private int page; // 현재 페이지 번호
    private int size; // 한 페이지에 포함된 레시피 수
    private int totalPages; // 전체 페이지 수
    private Long totalRecipes; // 전체 레시피 수
    private List<RecipeResponse> recipes; // 레시피 목록
}

