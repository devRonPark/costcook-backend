package com.costcook.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignUpOrLoginResponse {
    private String message;
    private String accessToken;
    private boolean isNewUser;

    public SignUpOrLoginResponse(String message) {
        this.message = message;
    }
}