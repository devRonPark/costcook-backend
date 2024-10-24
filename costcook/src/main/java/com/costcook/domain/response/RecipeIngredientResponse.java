package com.costcook.domain.response;

import com.costcook.entity.Ingredient;
import com.costcook.entity.RecipeIngredient;
import com.costcook.entity.Unit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeIngredientResponse {
	

	// 레시피별재료 테이블에서 재료 이름, 양, 단위, 가격 추출
	private String ingredientName;
	private String unitName;
	private double quantity;
	private int price;
	
	// response 변환
	public static RecipeIngredientResponse toDTO(RecipeIngredient recipeIngredient) {
		Ingredient ingredient = recipeIngredient.getIngredient();
		Unit unit = ingredient.getUnit();
		// 레시피 별 재료의 가격 계산 (재료값 * 양)
		int calculatePrice = (ingredient.getPrice() * (int) recipeIngredient.getQuantity());
		
		return RecipeIngredientResponse.builder()
				.ingredientName(ingredient.getName())
				.unitName(unit.getName())
				.quantity(recipeIngredient.getQuantity())
				.price(calculatePrice)
				.build();
	}
	
}
