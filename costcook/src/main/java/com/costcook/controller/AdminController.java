package com.costcook.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.costcook.domain.request.AdminRecipeRegisterRequest;
import com.costcook.domain.response.IngredientSearchResponse;
import com.costcook.domain.response.RecipeIngredientResponse;
import com.costcook.service.AdminRecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

  private final AdminRecipeService adminRecipeService;

  @GetMapping("/ingredients")
  public ResponseEntity<List<IngredientSearchResponse>> searchIngredientList(@RequestParam("keyword") String keyword) {

    log.info("Keyword received: {}", keyword);

    List<IngredientSearchResponse> responseList = adminRecipeService.searchIngredientsByName(keyword);

    return ResponseEntity.ok(responseList);
  }

  @GetMapping("/recipes/{recipeId}/ingredients")
  public ResponseEntity<List<RecipeIngredientResponse>> getRecipeIngredients(@PathVariable("recipeId") Long recipeId) {
    try {
      // 서비스에서 이미 변환된 DTO 리스트를 받아옵니다.
      List<RecipeIngredientResponse> ingredientResponses = adminRecipeService.findIngredientsByRecipeId(recipeId);
      return ResponseEntity.ok(ingredientResponses);
    } catch (Exception e) {
      log.error("레시피 재료 조회 중 오류 발생: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PostMapping("/recipes")
  public ResponseEntity<String> createRecipe(
    @ModelAttribute AdminRecipeRegisterRequest recipe,
    @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {

    // [로그] 레시피 주요 정보 
    log.info("레시피 등록 요청 - 제목: {}, 고유번호: {}, 카테고리ID: {}", 
      recipe.getTitle(), recipe.getRcpSno(), recipe.getCategoryId());

    // [로그] 썸네일 파일 이름
    if (thumbnailFile != null) {
      log.info("파일 이름: " + thumbnailFile.getOriginalFilename());
    }

    // 레시피 등록 로직 수행
    boolean result = adminRecipeService.saveRecipe(recipe, thumbnailFile);

    // [예외] 레시피 등록에 실패하면 IllegalStateException 발생.
    if (!result) {
      throw new IllegalStateException("레시피 등록에 실패했습니다."); 
    }

    // [로그] 레시피 등록 성공
    log.info("레시피 등록 완료 : " + recipe.getTitle());

    // OK 응답 반환
    return ResponseEntity.ok("레시피가 성공적으로 등록되었습니다.");
  }


  @PatchMapping("/recipes/{recipeId}")
  public ResponseEntity<String> updateRecipe(
    @PathVariable("recipeId") Long recipeId,
    @ModelAttribute AdminRecipeRegisterRequest recipe,
    @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile) {
    
    // [로그] 레시피 ID 확인
    log.info("레시피 수정 요청 - 레시피 ID: {}", recipeId);
    
    // [로그] 썸네일 파일 확인
    if (thumbnailFile != null) {
      log.info("새로운 썸네일 파일 이름: {}", thumbnailFile.getOriginalFilename());
    }
  
    // 레시피 수정 로직
    boolean result = adminRecipeService.updateRecipe(recipeId, recipe, thumbnailFile);
  
    // [예외] 레시피 수정에 실패하면 IllegalStateException 발생.
    if (!result) {
      throw new IllegalStateException("레시피 수정에 실패했습니다."); 
    }
  
    // [로그] 레시피 수정 완료
    log.info("레시피 수정 완료 - 레시피 ID: {}", recipeId);

    // OK 응답 반환
    return ResponseEntity.ok("레시피가 성공적으로 수정되었습니다.");
  }

  
  @DeleteMapping("/recipes/{recipeId}")
  public ResponseEntity<String> deleteRecipe(@PathVariable("recipeId") Long recipeId) {
    try {
      adminRecipeService.deleteRecipe(recipeId);
      return ResponseEntity.ok("레시피가 성공적으로 삭제되었습니다.");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("레시피를 잦을 수 없습니다.");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("레시피 삭제 중 오류가 발생했습니다.");
    }
  }

}
