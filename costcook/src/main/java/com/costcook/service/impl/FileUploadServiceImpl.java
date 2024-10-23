package com.costcook.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.costcook.service.FileUploadService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.io.InputStream;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    
    @Value("${spring.upload.recipe-location}")
    private String recipeUploadLocation;

    @Value("${spring.upload.user-location}")
    private String userUploadLocation;

    @PostConstruct
    public void init() {
        // 레시피 업로드 디렉토리 생성
        File recipeDirectory = new File(recipeUploadLocation);
        if (!recipeDirectory.exists()) {
            recipeDirectory.mkdirs(); // 상위 디렉토리까지 생성
        }

        // 사용자 업로드 디렉토리 생성
        File userDirectory = new File(userUploadLocation);
        if (!userDirectory.exists()) {
            userDirectory.mkdirs(); // 상위 디렉토리까지 생성
        }
    }

    // 파일 업로드 메서드 - 레시피용
    @Override
    public String uploadRecipeFile(MultipartFile file) {
        return saveFile(file, recipeUploadLocation);
    }

    // 파일 업로드 메서드 - 사용자용
    @Override
    public String uploadUserFile(MultipartFile file) {
        return saveFile(file, userUploadLocation);
    }

    // 파일 저장 메소드 - 저장된 파일명 리턴.
    private String saveFile(MultipartFile file, String uploadLocation) {
        try {
			// 원본 파일명 가져오기
			String originalFileName = file.getOriginalFilename();

			// 새로운 파일명 만들어주기
			String savedFileName = UUID.randomUUID() + "_" + originalFileName;
			
			// 파일 저장
			InputStream inputStream = file.getInputStream();	
			Path path = Paths.get(uploadLocation).resolve(savedFileName); // 저장 경로 설정
			
			// 이미 있는 경우 덮어쓰기
			Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            return savedFileName; // 저장된 파일명 반환
		} catch (IOException e) {
			e.printStackTrace();
			// 이상 있으면 null 반환
			return null;
		}
    }
}
