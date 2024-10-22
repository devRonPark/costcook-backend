package com.costcook.service;

import java.util.Optional;

import com.costcook.domain.PlatformTypeEnum;
import com.costcook.domain.request.SignUpOrLoginRequest;
import com.costcook.domain.response.SignUpOrLoginResponse;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	public Optional<String> getEmailFromSocialAccount(String socialKey, PlatformTypeEnum provider);
	public SignUpOrLoginResponse signUpOrLogin(SignUpOrLoginRequest request, HttpServletResponse response);
	public String refreshAccessToken(String refreshToken);
}
