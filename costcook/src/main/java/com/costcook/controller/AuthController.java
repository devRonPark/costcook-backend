package com.costcook.controller;

import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.EmailRequest;
import com.costcook.domain.request.VerificationRequest;
import com.costcook.service.EmailService;
import com.costcook.util.EmailUtil;

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
		if (!EmailUtil.isValidEmail(email)) {
			throw new IllegalArgumentException("유효하지 않은 이메일입니다.");
		}

		// 인증 코드 생성
		String verificationCode = EmailUtil.generateVerificationCode();

		try {
			emailService.sendVerificationCode(email, verificationCode);
			return ResponseEntity.ok().body("인증번호가 이메일로 전송되었습니다.");
		} catch (Exception e) {
			throw new RuntimeException("이메일 전송에 실패했습니다. 다시 시도해주세요.");
		}
	}

	@PostMapping("/verify-code")
	public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest request) {
		boolean isVerified = emailService.verifyCode(request.getEmail(), request.getVerificationCode());

		if (isVerified) {
			return ResponseEntity.ok().body(Map.of("isVerified", true, "message", "인증번호가 일치합니다."));
		} else {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}

	}
}
