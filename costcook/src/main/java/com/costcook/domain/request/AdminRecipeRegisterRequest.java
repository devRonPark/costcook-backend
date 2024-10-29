package com.costcook.domain.request;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AdminRecipeRegisterRequest {

  // 레시피 제목
  private String title;

  // 레시피 만개번호
  private int rcpSno = 0;

  // 레시피 설명
  private String description;

  // 카테고리 ID
  private Long categoryId;

  // 몇 인분인지
  private Integer servings = 1;

  // 재료 리스트
  private List<IngredientDTO> ingredients;

  // 썸네일 이미지 URL
  private String thumbnailUrl;

  // 총 가격
  private Integer price;

  // 썸네일 삭제 여부 플래그 추가
  private boolean thumbnailDeleted;

  @Getter
  @Setter
  public static class IngredientDTO {
      // 재료 ID
      private Long ingredientId;

      // 재료 양
      private double quantity;
  }

}