package com.costcook.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultRecommendedRecipe {
    private Long id;
    private boolean isUsed;
}