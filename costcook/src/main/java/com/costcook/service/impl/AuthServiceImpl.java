package com.costcook.service.impl;

import org.springframework.stereotype.Service;

import com.costcook.domain.PlatformTypeEnum;
import com.costcook.domain.request.SignUpOrLoginRequest;
import com.costcook.domain.request.SignUpOrLoginResponse;
import com.costcook.entity.SocialAccount;
import com.costcook.entity.User;
import com.costcook.exceptions.InvalidProviderException;
import com.costcook.exceptions.MissingFieldException;
import com.costcook.repository.SocialAccountRepository;
import com.costcook.repository.UserRepository;
import com.costcook.service.AuthService;
import com.costcook.util.OAuth2Properties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final SocialAccountRepository socialAccountRepository;
	
	@Override
	public SignUpOrLoginResponse signUpOrLogin(SignUpOrLoginRequest request) {
		// 1. Request DTO 유효성 검증
		validateSignUpOrLoginRequest(request);

		// 2. email 필드를 기준으로 회원 가입 여부 확인
		// - 회원이 이미 존재하는 경우 로그인 처리
		// - 신규 회원인 경우 회원가입 처리
		User user = userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> registerNewUser(request));

		log.info("새로 가입한 회원 정보: {}", user);
		// 3. 액세스 토큰, 리프레시 토큰 발급
		return null;
	}

	private void validateSignUpOrLoginRequest(SignUpOrLoginRequest request) {
		if (request.getSocialKey() == null || request.getEmail() == null || request.getProvider() == null) {
			throw new MissingFieldException();
		}

		if (!isValidProvider(request.getProvider())) {
			throw new InvalidProviderException("적절하지 않은 소셜 로그인 제공자입니다.");
		}
	}

	private boolean isValidProvider(String provider) {
		return provider.equals("kakao") || provider.equals("google");
	}
	
	private User registerNewUser(SignUpOrLoginRequest request) {
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        // 추가적인 사용자 정보 설정...
        User savedUser = userRepository.save(newUser);

        // 소셜 계정 정보 저장
        SocialAccount socialAccount = SocialAccount
        	.builder()
        	.provider(PlatformTypeEnum.fromString(request.getProvider()))
        	.socialKey(request.getSocialKey())
        	.user(savedUser)
        	.build();
        socialAccountRepository.save(socialAccount);

        return savedUser;
    }

}
