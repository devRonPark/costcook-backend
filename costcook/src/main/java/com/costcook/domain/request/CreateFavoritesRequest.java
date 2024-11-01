package com.costcook.domain.request;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateFavoritesRequest {
    private List<Long> recipeIds;
}
