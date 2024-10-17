package com.costcook.util;

import java.util.Random;
import org.springframework.util.StringUtils;

public class EmailUtil {

    // 이메일 유효성 검증 메서드
    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    // 6자리 인증 코드 생성 메서드
    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 6자리 랜덤 숫자 생성
        return String.valueOf(code);
    }
}
