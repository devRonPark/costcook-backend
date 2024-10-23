package com.costcook.domain.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UserUpdateRequest {

    private String nickname; // 필수

    private MultipartFile profileImage; // 선택

    private List<Integer> preferences; // 필수

    private List<Integer> dislikedIngredients; // 필수

    private boolean personalInfoAgreement; // 필수
}
