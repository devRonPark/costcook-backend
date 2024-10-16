package com.costcook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.OAuthUserInfo;
import com.costcook.domain.PlatformTypeEnum;
import com.costcook.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {
	private final UserService userService;
	
	@GetMapping("")
	public ResponseEntity<?> test() {
		return ResponseEntity.ok("hello world");
	}

	@GetMapping("/{provider}")
	public ResponseEntity<?> getProviderInfo(@RequestParam("code") final String code, @PathVariable("provider") final String provider, HttpServletResponse res) {
		log.info("들어온 코드 값 > {}, {}", code, provider);
		PlatformTypeEnum platformType = PlatformTypeEnum.fromString(provider);
		OAuthUserInfo oAuthUserInfo = userService.getOAuthUserInfo(code, platformType);
		return ResponseEntity.ok(oAuthUserInfo);
		
	}
}
