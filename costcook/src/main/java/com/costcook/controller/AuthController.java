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

import com.costcook.service.AuthService;
import com.costcook.domain.request.EmailRequest;
import com.costcook.domain.request.SignUpOrLoginRequest;
import com.costcook.domain.request.VerificationRequest;
import com.costcook.domain.response.SignUpOrLoginResponse;
import com.costcook.domain.response.VerifyCodeResponse;
import com.costcook.service.EmailService;
import com.costcook.util.EmailUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
    private final EmailService emailService;
	
	@PostMapping("/signup-or-login")
    public ResponseEntity<?> signUpOrLogin(@RequestBody SignUpOrLoginRequest signUpOrLoginRequest, HttpServletResponse response) {
		SignUpOrLoginResponse signUpOrLoginResponse = authService.signUpOrLogin(signUpOrLoginRequest);
    	return ResponseEntity.ok(signUpOrLoginResponse);
    }

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
			 return ResponseEntity.ok(new VerifyCodeResponse(true, "인증번호가 일치합니다."));
		} else {
			 return ResponseEntity.ok(new VerifyCodeResponse(false, "인증번호가 일치하지 않습니다."));
		}

	}
}
