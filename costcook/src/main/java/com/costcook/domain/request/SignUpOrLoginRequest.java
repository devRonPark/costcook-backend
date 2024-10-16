package com.costcook.domain.request;

import lombok.Data;

@Data
public class SignUpOrLoginRequest {
    private String socialKey;        // provider_user_id
    private String email;      // 사용자 이메일
    private String provider;   // 소셜 로그인 제공자 (kakao, google)
}