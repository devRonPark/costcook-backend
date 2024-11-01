package com.costcook.domain.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FavoriteListResponse {

	private List<FavoriteResponse> favorites;
	
}
