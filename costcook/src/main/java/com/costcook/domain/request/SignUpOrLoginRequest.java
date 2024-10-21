package com.costcook.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpOrLoginRequest {

    // 소셜 로그인 키 (Google, Kakao 등의 고유 식별자)
    private String socialKey;

    // 이메일 주소
    private String email;

    // 제공자 (소셜 로그인 제공자, 예: 'KAKAO', 'GOOGLE')
    private String provider;
}

