package com.costcook.domain.response;

import lombok.Data;
import java.util.List;

@Data
public class ReviewListResponse {
    private int page; // 현재 페이지 번호
    private int size; // 한 페이지에 포함된 리뷰 수
    private int totalPages; // 전체 페이지 수
    private long totalReviews; // 전체 리뷰 수
    private List<ReviewResponse> reviews; // 리뷰 목록
}

