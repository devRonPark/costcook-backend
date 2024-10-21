package com.costcook.domain.response;

import lombok.Data;

@Data
public class SignUpOrLoginResponse {
    private String message;
    private String accessToken;
    private String refreshToken;
    private boolean isNewUser;

    public SignUpOrLoginResponse(String message) {
        this.message = message;
    }

    public SignUpOrLoginResponse(String message, String accessToken, String refreshToken, boolean isNewUser) {
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isNewUser = isNewUser;
    }
}