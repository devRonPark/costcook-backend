package com.costcook.domain.response;

import lombok.Data;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

import com.costcook.entity.Ingredient;

@Data
public class IngredientListResponse {

    private List<IngredientResponse> ingredients;

    // Entity 리스트 -> DTO 리스트로 변환하는 메서드
    public static IngredientListResponse toDTO(List<Ingredient> ingredientList) {
        List<IngredientResponse> ingredientResponses = ingredientList.stream()
                .map(IngredientResponse::toDTO)
                .collect(Collectors.toList());

        IngredientListResponse response = new IngredientListResponse();
        response.setIngredients(ingredientResponses);
        return response;
    }

    @Data
    @Builder
    public static class IngredientResponse {
        private Long id;
        private String name;
        private String categoryName;
        private String unitName;
        private Double pricePerUnit;

        // Entity -> DTO 변환하는 빌더 메서드
        public static IngredientResponse toDTO(Ingredient ingredient) {
            return IngredientResponse.builder()
                    .id(ingredient.getId())
                    .name(ingredient.getName())
                    .categoryName(ingredient.getCategory() != null ? ingredient.getCategory().getName() : "기타")  // 카테고리 이름이 없을 때 "기타" 반환
                    .unitName(ingredient.getUnit().getName())      // 단위 이름
                    .pricePerUnit((double) ingredient.getPrice())  // 단위당 가격
                    .build();
        }
    }

}
