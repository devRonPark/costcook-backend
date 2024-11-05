package com.costcook.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.costcook.domain.request.AdminIngredientRegisterRequest;
import com.costcook.domain.request.AdminRecipeRegisterRequest;
import com.costcook.domain.response.AdminIngredientResponse;
import com.costcook.domain.response.RecipeIngredientResponse;
import com.costcook.domain.response.ReviewListResponse;
import com.costcook.service.AdminIngredientService;
import com.costcook.service.AdminRecipeService;
import com.costcook.service.AdminReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

  private final AdminRecipeService adminRecipeService;
  private final AdminIngredientService adminIngredientService;
  private final AdminReviewService adminReviewService;

  @GetMapping("/ingredients")
  public ResponseEntity<List<AdminIngredientResponse>> getAllIngredients() {
    log.info("모든 재료를 가져오라는 요청을 받았습니다.");

    List<AdminIngredientResponse> ingredientList = adminIngredientService.getAllIngredients();

    return ResponseEntity.ok(ingredientList);
  }
  

  @GetMapping("/ingredients/search")
  public ResponseEntity<List<AdminIngredientResponse>> searchIngredients(@RequestParam("keyword") String keyword) {

    log.info("Keyword received: {}", keyword);

    List<AdminIngredientResponse> ingredientList = adminIngredientService.searchIngredientsByName(keyword);

    return ResponseEntity.ok(ingredientList);
  }

  
  @GetMapping("/ingredients/duplicate")
  public ResponseEntity<Map<String, Boolean>> checkDuplicateIngredient(
    @RequestParam("ingredientName") String ingredientName) {

    boolean exists = adminIngredientService.isIngredientDuplicate(ingredientName);

    Map<String, Boolean> response = new HashMap<>();
    response.put("exists", exists);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/ingredients")
  public ResponseEntity<String> saveIngredient(
    @ModelAttribute AdminIngredientRegisterRequest ingredient) {

    // [로그] 재료 이름 확인
    log.info("재료 등록 요청 - {}", ingredient.getName());

    // 재료 등록 로직
    boolean result = adminIngredientService.saveIngredient(ingredient);

    // [예외] 재료 등록에 실패하면 IllegalStateException 발생.
    if (!result) {
      throw new IllegalStateException("재료 등록에 실패했습니다."); 
    }

    // [로그] 재료 등록 성공
    log.info("재료 등록 완료 : " + ingredient.getName());

    return ResponseEntity.ok("재료가 성공적으로 등록되었습니다.");

  }


  @PatchMapping("/ingredients/{ingredientId}")
  public ResponseEntity<String> editIngredient(
    @PathVariable("ingredientId") Long ingredientId,
    @ModelAttribute AdminIngredientRegisterRequest ingredient) {

    // [로그] 재료 ID 확인
    log.info("재료 수정 요청 - 재료 ID: {}", ingredientId);

    // 재료 수정 로직
    boolean result = adminIngredientService.updateIngredient(ingredientId, ingredient);

    // [예외] 재료 수정에 실패하면 IllegalStateException 발생.
    if (!result) {
      throw new IllegalStateException("재료 수정에 실패했습니다."); 
    }

    // [로그] 재료 수정 성공
    log.info("재료 수정 완료 - 재료 ID: {} ", ingredientId);

    return ResponseEntity.ok("재료가 성공적으로 수정되었습니다.");

  }

  @DeleteMapping("/ingredients/{ingredientId}")
  public ResponseEntity<String> deleteIngredient(
    @PathVariable("ingredientId") Long ingredientId) {

    boolean result = adminIngredientService.deleteIngredient(ingredientId);

    // [예외] 재료 삭제에 실패하면 IllegalStateException 발생.
    if(!result) {
      throw new IllegalStateException("재료 수정에 실패했습니다."); 
    }

    // [로그] 재료 수정 성공
    log.info("재료 삭제 완료 - 재료 ID: {} ", ingredientId);

    return ResponseEntity.ok("재료가 성공적으로 삭제되었습니다.");
  }


  @GetMapping("/recipes/{recipeId}/ingredients")
  public ResponseEntity<List<RecipeIngredientResponse>> getRecipeIngredients(@PathVariable("recipeId") Long recipeId) {
    try {
      List<RecipeIngredientResponse> ingredientResponses = adminRecipeService.findIngredientsByRecipeId(recipeId);
      return ResponseEntity.ok(ingredientResponses);
    } catch (Exception e) {
      log.error("레시피 재료 조회 중 오류 발생: " + e.getMessage(), e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/recipes/duplicate")
  public ResponseEntity<Map<String, Boolean>> checkDuplicateRecipe(
    @RequestParam("recipeTitle") String recipeTitle) {

    boolean exists = adminRecipeService.isRecipeDuplicate(recipeTitle);

    Map<String, Boolean> response = new HashMap<>();
    response.put("exists", exists);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/recipes")
  public ResponseEntity<String> saveRecipe(
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


  @GetMapping("/reviews")
  public ResponseEntity<ReviewListResponse> getReviewList(
    @RequestParam(name = "page", defaultValue = "1") int page,
    @RequestParam(name = "size", defaultValue = "5") int size,
    @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
    @RequestParam(name = "direction", defaultValue = "asc") String direction
  ) {
    // 전달된 파라미터 값을 로그로 기록
    log.info("리뷰 리스트 요청 받음 - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, direction);
  
    // 페이지가 1부터 시작하기 때문에 내부적으로 사용할 때는 -1로 조정
    ReviewListResponse response = adminReviewService.getReviewList(page - 1, size, sortBy, direction);
    
    return ResponseEntity.ok(response);
  }

}
