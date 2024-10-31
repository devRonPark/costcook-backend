package com.costcook.domain.request;

import lombok.Data;

@Data
public class AdminIngredientRegisterRequest {

  // 재료 이름
  private String name;

  // 카테고리 번호
  private Long categoryId;

  // 단위 번호
  private Long unitId;

  // 1단위 가격
  private int price;


}
