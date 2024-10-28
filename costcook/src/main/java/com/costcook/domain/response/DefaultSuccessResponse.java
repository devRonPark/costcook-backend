package com.costcook.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultSuccessResponse {
    private String message; // 에러 메시지
}