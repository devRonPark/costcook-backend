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
        log.info("내 정보 조회 API 호출");
        log.info("내 정보: {}", userDetails.toString());
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("액세스 토큰이 만료되었습니다.?"));
        }

        // UserDetailsImpl에서 사용자 정보를 추출
        UserResponse userResponse = UserResponse.from(userDetails);
        return ResponseEntity.ok(userResponse);
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
