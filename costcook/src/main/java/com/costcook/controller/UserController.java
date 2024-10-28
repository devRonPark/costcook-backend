package com.costcook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.UserUpdateRequest;
import com.costcook.domain.response.UserResponse;
import com.costcook.entity.User;
import com.costcook.exceptions.ErrorResponse;
import com.costcook.service.FileUploadService;
import com.costcook.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(
        @AuthenticationPrincipal User userDetails // 사용자 정보 가져오기
    ) {
        try {

            log.info("내 정보 조회 API 호출");
            log.info("내 정보: {}", userDetails.toString());
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse("액세스 토큰이 만료되었습니다.?"));
            }
    
            // UserServiceImpl 에서 userDetails.getId() 를 이용해서 각각 선호 재료 목록(preferredIngredients), 기피 재료 목록(dislikedIngredients) 조회
            // 파라미터 값: User user
            // 리턴 값: { preferredIngredients: [ ], dislikedIngredients: [ ] }
            Map<String, List<Long>> userTaste = userService.getPreferredAndDislikedCategoryIds(userDetails);
            log.info("{}", userTaste);
    
            // [] 혹은
            // [{"userId": 1, "categoryId": 2}, {"userId": 1, "categoryId": 3}, {"userId": 1, "categoryId": 4}, ...]
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

    @PatchMapping("/me")
    public ResponseEntity<String> updateMyInfo(
        @AuthenticationPrincipal User userDetails, // 사용자 정보 가져오기,
        @ModelAttribute UserUpdateRequest requestDTO
    ) {
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
}
