package com.costcook.domain.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFavoriteResponse {
    private String message;         // 응답 메시지
    private List<Long> recipeIds;   // 삭제된 레시피 ID 목록
}