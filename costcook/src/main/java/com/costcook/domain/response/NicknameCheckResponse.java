package com.costcook.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NicknameCheckResponse {
    private boolean nicknameDuplicated;
}
