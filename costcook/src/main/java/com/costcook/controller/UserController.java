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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

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
        log.info("내 정보 업데이트 API 호출");
        log.info("내 정보: {}", userDetails.toString());
        log.info("요청 본문 정보: {}", requestDTO.toString());

        // 1. 프로필 이미지 처리
        // - 프로필 이미지가 제공된 경우, 파일을 서버에 저장하고 URL을 업데이트합니다.
        // - 파일 저장 경로와 파일 이름을 지정하고, 필요 시 기존 이미지를 삭제합니다.
        
        // 2. 선호 재료 및 기피 재료 업데이트
        // - requestDTO에서 선호 재료(preferences)와 기피 재료(dislikedIngredients)를 추출합니다.
        // - 이를 사용자의 프로필에 업데이트합니다.
        // - 기존에 저장된 리스트가 있을 경우, 이를 업데이트하거나 새로 추가합니다.

        // 3. 개인 정보 동의 여부 처리
        // - 개인 정보 동의 여부를 확인하고, 필요한 경우 추가적인 로직을 실행합니다.
        // - 예를 들어, 동의가 필요한 경우 해당 정보를 데이터베이스에 업데이트합니다.

        // 4. 사용자 정보를 데이터베이스에 저장
        // - 위의 모든 정보를 업데이트한 후, 데이터베이스에 해당 정보를 저장합니다.
        // - 저장이 성공적으로 완료되면 커밋하고, 오류 발생 시 롤백합니다.

        // 5. 성공 응답 반환
        // - 모든 작업이 완료된 후, 성공적인 응답 메시지를 반환합니다.
        return ResponseEntity.ok("내 정보가 성공적으로 업데이트되었습니다.");
    }
}
