package com.costcook.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.costcook.domain.request.AdminRecipeRegisterRequest;
import com.costcook.domain.response.IngredientSearchResponse;
import com.costcook.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/ingredients")
  public ResponseEntity<List<IngredientSearchResponse>> getIngredientList(@RequestParam String keyword) {

    log.info("Keyword received: {}", keyword);

    List<IngredientSearchResponse> responseList = adminService.getIngredientsByName(keyword);

    return ResponseEntity.ok(responseList);
  }


  @PostMapping("/recipes")
  public ResponseEntity<String> createRecipe(
        @RequestPart("recipe") AdminRecipeRegisterRequest recipe,
        @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnailFile) {
    try {
      
      log.info("레시피 제목: " + recipe.getTitle());
      if (thumbnailFile != null) {
        log.info("파일 이름: " + thumbnailFile.getOriginalFilename());
      }

      return ResponseEntity.ok("레시피가 성공적으로 등록되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("레시피 등록에 실패했습니다.");
    }
  }

  

}
