package com.costcook.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.costcook.domain.response.SignUpOrLoginResponse;
import com.costcook.entity.User;
import com.costcook.security.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtils {
    private final JwtProvider jwtProvider;
    
    // 사용자 정보를 기반으로 액세스 토큰과 리프레시 토큰을 생성
    public Map<String, String> generateToken(User user) {
        log.info(user.toString());
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        log.info("{}, {}", accessToken, refreshToken);
        
        // 생성된 토큰을 Map에 담아 반환
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);        
        return tokenMap;
    }
    
    // JSON 형식의 응답을 전송
    public void writeResponse(HttpServletResponse response, SignUpOrLoginResponse signUpOrLoginResponse) throws IOException {        
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(signUpOrLoginResponse);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
    
    // 쿠키 설정을 위한 기본 메소드
    public void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(name.equals("refreshToken") ? true : false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
    
    // 리프레시 토큰을 쿠키에 설정하는 메소드
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        setCookie(response, "refreshToken", refreshToken, 1 * 24 * 60 * 60); // 1일 유효기간
    }

    // 액세스 토큰을 쿠키에 설정하는 메소드
    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        setCookie(response, "accessToken", accessToken, 30 * 60); // 30분 유효기간
    }

    // 리프레시 토큰을 쿠키에서 제거하는 메소드
    public void removeRefreshTokenCookie(HttpServletResponse response) {
        setCookie(response, "refreshToken", "", 0); // 유효기간을 0으로 설정하여 쿠키 제거
    }
    public void removeAccessTokenCookie(HttpServletResponse response) {
      setCookie(response, "accessToken", "", 0); // 유효기간을 0으로 설정하여 쿠키 제거
  }
}
