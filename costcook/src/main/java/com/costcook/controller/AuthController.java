package com.costcook.controller;

import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.EmailRequest;
import com.costcook.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final EmailService emailService;
    
    @PostMapping("/send-verification-code")
    public ResponseEntity<?> sendVerificationCode(@RequestBody EmailRequest emailRequest) {
    	String email = emailRequest.getEmail();
    	
    	// 이메일 유효성 검증
        if (!isValidEmail(email)) {
            return ResponseEntity.badRequest().body("유효하지 않은 이메일입니다.");
        }

        // 인증 코드 생성
        String verificationCode = generateVerificationCode();

        try {
            emailService.sendVerificationCode(email, verificationCode);
            return ResponseEntity.ok().body("인증번호가 이메일로 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("이메일 전송에 실패했습니다. 다시 시도해주세요.");
        }
    }
    
    private boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 랜덤 숫자 생성
        return String.valueOf(code);
    }
}
