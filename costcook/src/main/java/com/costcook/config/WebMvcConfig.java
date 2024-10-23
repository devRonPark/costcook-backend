package com.costcook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
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
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 레시피 이미지 경로 설정
        registry
            .addResourceHandler("/img/recipe/**")
            .addResourceLocations("file:" + recipeUploadLocation + "/");

        // 사용자 프로필 이미지 경로 설정
        registry
            .addResourceHandler("/img/user/**")
            .addResourceLocations("file:" + userUploadLocation + "/");
    }
}