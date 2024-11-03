package com.costcook.domain.response;

import java.util.List;

import lombok.Data;

@Data
public class CreateFavoritesResponse {
    private List<FavoriteResponse> favorites;

    // 생성자
    public CreateFavoritesResponse(List<FavoriteResponse> favorites) {
        this.favorites = favorites;
    }
}
