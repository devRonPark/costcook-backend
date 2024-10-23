package com.costcook.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    public String uploadRecipeFile(MultipartFile file);
    public String uploadUserFile(MultipartFile file);
}
