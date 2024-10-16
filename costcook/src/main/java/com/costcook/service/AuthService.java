package com.costcook.service;

import com.costcook.domain.request.SignUpOrLoginRequest;
import com.costcook.domain.request.SignUpOrLoginResponse;

public interface AuthService {
	public SignUpOrLoginResponse signUpOrLogin(SignUpOrLoginRequest request);
}
