package com.costcook.service;

import com.costcook.domain.request.SignUpOrLoginRequest;
import com.costcook.domain.response.SignUpOrLoginResponse;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	public SignUpOrLoginResponse signUpOrLogin(SignUpOrLoginRequest request, HttpServletResponse response);
}
