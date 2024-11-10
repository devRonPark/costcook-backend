package com.costcook.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	// application.yml 파일의 location 정보 가져오기
    @Value("${spring.upload.recipe-location}")
    private String recipeUploadLocation;

    @Value("${spring.upload.user-location}")
    private String userUploadLocation;

    @Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
			.addMapping("/**")
			.allowedOrigins("http://localhost:3000", "https://costcook.shop")
			.allowedMethods("OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
			.allowedHeaders("*") // 모든 헤더 허용
            .allowCredentials(true); // 자격 증명 허용 (쿠키, 인증 정보)
	}

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 레시피 이미지 경로 설정
        registry
            .addResourceHandler("/api/img/recipe/**")
            .addResourceLocations("file:" + recipeUploadLocation + "/")
            .setCachePeriod(2592000); // 30일 동안 캐시 처리

        // 사용자 프로필 이미지 경로 설정
        registry
            .addResourceHandler("/api/img/user/**")
            .addResourceLocations("file:" + userUploadLocation + "/");
    }
}