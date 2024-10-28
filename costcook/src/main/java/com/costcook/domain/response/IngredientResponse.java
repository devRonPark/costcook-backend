package com.costcook.domain.response;

import com.costcook.entity.Category;

import lombok.Builder;
import lombok.Data;

// 재료명, 양, 단위, 가격

@Data
@Builder
public class IngredientResponse {

	private Long ingredientId;
    private String ingredientName;
    private Category category;	
    private String unitName;
    private int price;
    private double quantity;
}
