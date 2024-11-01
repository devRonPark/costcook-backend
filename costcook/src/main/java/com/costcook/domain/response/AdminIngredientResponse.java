package com.costcook.domain.response;

import lombok.Data;
import lombok.Builder;

import com.costcook.entity.Ingredient;
import com.costcook.entity.Unit;
import com.costcook.entity.Category;;

@Data
@Builder
public class AdminIngredientResponse {

  private Long id;
  private String name;
  private Category category;
  private Unit unit;
  private Double pricePerUnit;

  // Entity -> DTO 변환하는 빌더 메서드
  public static AdminIngredientResponse toDTO(Ingredient ingredient) {
    return AdminIngredientResponse.builder()
            .id(ingredient.getId())
            .name(ingredient.getName())
            .category(ingredient.getCategory())
            .unit(ingredient.getUnit())
            .pricePerUnit((double) ingredient.getPrice())  // 단위당 가격
            .build();
    }
}

