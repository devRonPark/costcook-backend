package com.costcook.domain.request;

import com.costcook.domain.RoleEnum;

import lombok.Data;

@Data
public class AdminSignupRequest {
    private String email;
    private String password;
    private RoleEnum role;
}