package com.costcook.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.RecommendedRecipeRequest;
import com.costcook.domain.request.UserUpdateRequest;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.domain.response.ReviewResponse;
import com.costcook.domain.response.UserResponse;
import com.costcook.domain.response.WeeklyRecipesResponse;
import com.costcook.entity.User;
import com.costcook.exceptions.ErrorResponse;
import com.costcook.service.RecipeService;
import com.costcook.service.ReviewService;
import com.costcook.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final ReviewService reviewService;
	private final RecipeService recipeService;

	@GetMapping("/me")
	public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal User userDetails // 사용자 정보 가져오기
	) {
		try {

			log.info("내 정보 조회 API 호출");
			log.info("내 정보: {}", userDetails.toString());
			if (userDetails == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("액세스 토큰이 만료되었습니다.?"));
			}

			// UserServiceImpl 에서 userDetails.getId() 를 이용해서 각각 선호 재료
			// 목록(preferredIngredients), 기피 재료 목록(dislikedIngredients) 조회
			// 파라미터 값: User user
			// 리턴 값: { preferredIngredients: [ ], dislikedIngredients: [ ] }
			Map<String, List<Long>> userTaste = userService.getPreferredAndDislikedCategoryIds(userDetails);
			log.info("{}", userTaste);

			// [] 혹은
			// [{"userId": 1, "categoryId": 2}, {"userId": 1, "categoryId": 3}, {"userId":
			// 1, "categoryId": 4}, ...]
			// > [2, 3, 4] // categoryId 만 담긴 배열로 전환 필요.
			userTaste.get("preferredIngredient");
			userTaste.get("dislikedIngredient");

			// UserDetailsImpl에서 사용자 정보를 추출
			UserResponse userResponse = UserResponse.from(userDetails, userTaste);
			return ResponseEntity.ok(userResponse);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("{}", e.getMessage());
			throw e;
		}
	}

	// 내 리뷰 목록 조회
	// 단, 액세스 토큰이 없거나 만료된 액세스 토큰과 함께 요청시 403 Forbidden 에러 발생.
	@GetMapping("/me/reviews")
	public ResponseEntity<?> getMyReviews(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "recipeId", required = false) Long recipeId,
		@AuthenticationPrincipal User userDetails // 사용자 정보 가져오기
	) {
		if (recipeId != null) {
			ReviewResponse userReview = reviewService.getReviewByUserAndRecipe(userDetails, recipeId);
			return ResponseEntity.ok(userReview); // 특정 레시피 리뷰 반환
		}
		ReviewListResponse response = reviewService.getReviewListByUserWithPagination(userDetails, page);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/me")
	public ResponseEntity<String> updateMyInfo(@AuthenticationPrincipal User userDetails, // 사용자 정보 가져오기,
			@ModelAttribute UserUpdateRequest requestDTO) {
		try {
			log.info("내 정보 업데이트 API 호출");
			log.info("내 정보: {}", userDetails.toString());
			log.info("요청 본문 정보: {}", requestDTO.toString());

			// 사용자 정보 업데이트 로직 서비스에 위임
			userService.updateUserInfo(userDetails, requestDTO);

			// 성공 응답 반환
			return ResponseEntity.ok("내 정보가 성공적으로 업데이트되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("{}", e.getMessage());
			throw e;
		}
	}

	// 추천 레시피 가져오기

	@GetMapping("/me/recommended-recipes")
	public ResponseEntity<WeeklyRecipesResponse> getAllRecommendRecipes(@RequestParam(name = "year") int year,
			@RequestParam(name = "weekNumber") int weekNumber, @AuthenticationPrincipal User user) {

		List<WeeklyRecipesResponse.Recipe> recommendedRecipes = recipeService.getRecommendedRecipes(year, weekNumber,
				user);

		WeeklyRecipesResponse response = WeeklyRecipesResponse.builder().year(year).weekNumber(weekNumber)
				.recipes(recommendedRecipes).build();

		return ResponseEntity.ok(response);
	}
	
	// used가 true인 레시피 가져오기
    @GetMapping("/me/used-recipes")
    public ResponseEntity<?> getUsedRecommendedRecipes(@RequestParam(name = "year") int year,
			@RequestParam(name = "weekNumber") int weekNumber, @AuthenticationPrincipal User user) {
        List<WeeklyRecipesResponse.Recipe> usedRecipes = recipeService.getUsedRecommendedRecipes(year, weekNumber, user);
        
        WeeklyRecipesResponse response = WeeklyRecipesResponse.builder().year(year).weekNumber(weekNumber)
				.recipes(usedRecipes).build();
        
        return ResponseEntity.ok(response);
    }
    
    

}
