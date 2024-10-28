package com.costcook.domain.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UserUpdateRequest {
    private String nickname; // 선택
    private MultipartFile profileImage; // 선택
    private List<Long> preferredIngredients; // 선택
    private List<Long> dislikedIngredients; // 선택
    private Boolean personalInfoAgreement; // 선택
}
