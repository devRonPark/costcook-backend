package com.costcook.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.costcook.domain.response.ReviewListResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.entity.Review;
import com.costcook.repository.ReviewRepository;
import com.costcook.service.AdminReviewService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminReviewServiceImpl implements AdminReviewService {

	private final ReviewRepository reviewRepository;

  @Override
  public ReviewListResponse getReviewList(Map<String, String> params) {
    int page = Integer.parseInt(params.getOrDefault("page", "0"));
    int size = Integer.parseInt(params.getOrDefault("size", "5"));
    String sortBy = params.getOrDefault("sortBy", "id");
    String direction = params.getOrDefault("direction", "asc");
    String category = params.getOrDefault("category", "");
    String query = params.getOrDefault("query", "");

    // 기본 정렬 설정
    Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

    // 페이지네이션을 위한 Pageable 객체 생성 (페이지 번호는 0부터 시작)
    Pageable pageable = PageRequest.of(page, size, sort);
    
    Page<Review> reviewPage;

    // 카테고리 조건에 따라 적절한 쿼리 호출
    if ("레시피".equals(category) && !query.isEmpty()) {
      reviewPage = reviewRepository.findActiveReviewByRecipeTitleContaining(query, pageable);
    } else if ("작성자".equals(category) && !query.isEmpty()) {
      reviewPage = reviewRepository.findActiveReviewByUserNicknameContaining(query, pageable);
    } else {
      reviewPage = reviewRepository.findActiveReviews(pageable);
    }

    // Page 객체에서 필요한 정보 추출
    List<Review> reviews = reviewPage.getContent();
    long totalElements = reviewPage.getTotalElements();
    int totalPages = reviewPage.getTotalPages();
    int currentSize = reviewPage.getNumberOfElements();

    // ReviewListResponse 객체에 데이터를 담아 반환
    return ReviewListResponse.builder()
            .reviews(reviews.stream().map(ReviewResponse::toDTO).collect(Collectors.toList()))
            .totalReviews(totalElements)
            .totalPages(totalPages)
            .page(page)
            .size(currentSize)
            .build();
  }

  @Override
  public boolean updateReviewStatus(Long reviewId) {
    // 리뷰 조회 (존재하지 않으면 예외 발생)
    Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

    // 상태 변경
    review.setStatus(!review.isStatus());

    // 변경된 리뷰 저장
    reviewRepository.save(review);

    log.info("리뷰 상태 변경 완료 - 리뷰 ID: {}, 새로운 상태: {}", reviewId, review.isStatus());

    return true;
  }


}
