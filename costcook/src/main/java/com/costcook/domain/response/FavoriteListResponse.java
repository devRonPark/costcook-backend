package com.costcook.domain.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FavoriteListResponse {
    private int page; // 현재 페이지 번호
	private int size; // 한 페이지에 포함된 즐겨찾기 수
    private int totalPages; // 전체 페이지 수
    private Long totalFavorites; // 전체 즐겨찾기 수
	private List<FavoriteResponse> favorites;
}
