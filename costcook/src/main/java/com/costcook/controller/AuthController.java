package com.costcook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.request.SignUpOrLoginRequest;
import com.costcook.service.AuthService;
import com.costcook.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/signup-or-login")
    public ResponseEntity<?> signUpOrLogin(@RequestBody SignUpOrLoginRequest signUpOrLoginRequest) {
		// 1. authRequest 필드 유효성 검증
		authService.signUpOrLogin(signUpOrLoginRequest);
		
		
    	return ResponseEntity.ok("signup or login");
    }
}
