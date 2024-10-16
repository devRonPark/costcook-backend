package com.costcook.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	// HTTP 요청에 따른 보안 구성
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// 경로 권한 설정
		http.authorizeHttpRequests(auth ->
			// 특정 URL 경로에 대해서는 인증 없이 접근 가능
			auth.requestMatchers(
				new AntPathRequestMatcher("/api/oauth/**"),
				new AntPathRequestMatcher("/api/auth/signup")
			).permitAll()
			// 그 밖의 다른 요청들은 인증을 통과한(로그인한) 사용자라면 모두 접근할 수 있도록 한다.
			.anyRequest().authenticated()
		);
		
		// 무상태성 세션 관리
		http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		// HTTP 기본 설정
		http.httpBasic(HttpBasicConfigurer::disable);
		// CSRF 비활성화
		http.csrf(AbstractHttpConfigurer::disable);
		// CORS 설정
		http.cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()));
		
		return http.getOrBuild();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		return request -> {
			CorsConfiguration config = new CorsConfiguration();
			config.setAllowedHeaders(Collections.singletonList("*"));
			config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
			config.setAllowedOrigins(List.of("http://localhost:3000"));
			config.setAllowCredentials(true);
			return config;
		};
	}
}
