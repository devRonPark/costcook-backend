package com.costcook.domain.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import com.costcook.entity.RecipeIngredient;

@Data
@Builder
public class RecipeIngredientResponse {
    
    private Long ingredientId;
    private String name;
    private double quantity;
    private String unitName;
    private int pricePerUnit;

    public static RecipeIngredientResponse toDTO(RecipeIngredient recipeIngredient) {
        return RecipeIngredientResponse.builder()
                .ingredientId(recipeIngredient.getIngredient().getId())
                .name(recipeIngredient.getIngredient().getName())
                .quantity(recipeIngredient.getQuantity())
                .unitName(recipeIngredient.getIngredient().getUnit().getName())
                .pricePerUnit(recipeIngredient.getIngredient().getPrice())
                .build();
    }

    public static List<RecipeIngredientResponse> toDTOList(List<RecipeIngredient> recipeIngredients) {
        return recipeIngredients.stream()
                .map(RecipeIngredientResponse::toDTO)
                .collect(Collectors.toList());
    }
}
