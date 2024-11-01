package com.costcook.domain.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateFavoriteRequest {
    private Long recipeId;
}
