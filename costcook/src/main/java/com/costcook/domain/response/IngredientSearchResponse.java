package com.costcook.domain.response;

import lombok.Data;
import lombok.Builder;

import com.costcook.entity.Ingredient;

@Data
@Builder
public class IngredientSearchResponse {

  private Long id;
  private String name;
  private String categoryName;
  private String unitName;
  private Double pricePerUnit;

    // Entity -> DTO 변환하는 빌더 메서드
  public static IngredientSearchResponse toDTO(Ingredient ingredient) {
    return IngredientSearchResponse.builder()
            .id(ingredient.getId())
            .name(ingredient.getName())
            .categoryName(ingredient.getCategory() != null ? ingredient.getCategory().getName() : "기타")  // 카테고리 이름이 없을 때 "기타" 반환
            .unitName(ingredient.getUnit().getName())      // 단위 이름
            .pricePerUnit((double) ingredient.getPrice())  // 단위당 가격
            .build();
    }
}

