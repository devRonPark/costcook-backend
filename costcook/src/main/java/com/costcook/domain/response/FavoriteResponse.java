package com.costcook.domain.response;

import com.costcook.entity.Favorite;
import com.costcook.entity.FavoriteId;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteResponse {
    private FavoriteId id; // 즐겨찾기 복합키
    private FavoriteRecipeResponse recipe; // 레시피 정보

    public static FavoriteResponse toDTO(Favorite favorite, int price, double avgRatings) {
        // 평점 소수점 첫째자리까지 반올림
		double roundedAvgRatings = Math.round(avgRatings * 10) / 10.0;

        // RecipeResponse는 기존 DTO를 그대로 사용
        FavoriteRecipeResponse recipeResponse = FavoriteRecipeResponse.builder()
                .id(favorite.getRecipe().getId())
                .title(favorite.getRecipe().getTitle())
                .price(price)
                .avgRatings(roundedAvgRatings)
                .thumbnailUrl(favorite.getRecipe().getThumbnailUrl())
                .build();

        return FavoriteResponse.builder()
                .id(favorite.getId())
                .recipe(recipeResponse)
                .build();
    }
}
