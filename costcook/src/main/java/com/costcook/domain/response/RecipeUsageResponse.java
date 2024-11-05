
package com.costcook.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeUsageResponse {
	private String message; // 성공 메시지
    private Long recipeId;  // 레시피 식별자
    private boolean used;    // 사용 여부
}
