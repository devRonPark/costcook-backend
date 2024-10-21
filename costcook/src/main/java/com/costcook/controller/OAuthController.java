package com.costcook.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.OAuthUserInfo;
import com.costcook.domain.PlatformTypeEnum;
import com.costcook.service.AuthService;
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
	private final AuthService authService;
	
	@GetMapping("")
	public ResponseEntity<?> test() {
		return ResponseEntity.ok("hello world");
	}

	@GetMapping("/{provider}")
	public ResponseEntity<OAuthUserInfo> getProviderInfo(@RequestParam("code") final String code, @PathVariable("provider") final String provider, HttpServletResponse res) {
		log.info("들어온 코드 값 > {}, {}", code, provider);
		PlatformTypeEnum platformType = PlatformTypeEnum.fromString(provider);
		OAuthUserInfo oAuthUserInfo = userService.getOAuthUserInfo(code, platformType);
//		이 데이터를 받기 전에 서버에서 socialKey, provider 로 social_accounts 테이블을 조회하고, social_accounts 테이블에 데이터가 있으면 user_id 를 찾아 users 테이블을 조회해서 email 값을 가져와야 한다.
		Optional<String> email = authService.getEmailFromSocialAccount(oAuthUserInfo.getSocialKey(), oAuthUserInfo.getProvider());
		if (email.isPresent()) oAuthUserInfo.setEmail(email.get());
		
		return ResponseEntity.ok(oAuthUserInfo);
	}
}
