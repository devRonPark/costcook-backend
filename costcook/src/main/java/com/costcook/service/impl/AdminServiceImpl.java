package com.costcook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.costcook.domain.request.AdminRecipeRegisterRequest;
import com.costcook.domain.request.AdminRecipeRegisterRequest.IngredientDTO;
import com.costcook.domain.response.IngredientSearchResponse;
import com.costcook.domain.response.RecipeIngredientResponse;
import com.costcook.entity.Category;
import com.costcook.entity.Ingredient;
import com.costcook.entity.RecipeIngredient;
import com.costcook.entity.RecipeItem;
import com.costcook.repository.CategoryRepository;
import com.costcook.repository.IngredientRepository;
import com.costcook.repository.RecipeIngredientRepository;
import com.costcook.repository.RecipeRepository;
import com.costcook.service.AdminService;
import com.costcook.service.FileUploadService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final IngredientRepository ingredientRepository;
  private final RecipeRepository recipeRepository;
  private final CategoryRepository categoryRepository;
  private final RecipeIngredientRepository recipeIngredientRepository;

  private final FileUploadService fileUploadService;
  private final String RECIPE_THUMBNAIL_ACCESS_PATH = "/img/recipes/";

  /**
   * 주어진 키워드를 사용하여 재료를 검색합니다.
   * 
   * @param keyword - 검색할 재료의 키워드
   * @return List<IngredientSearchResponse> - 검색된 재료 정보를 담은 DTO 리스트
   */
  @Override
  public List<IngredientSearchResponse> getIngredientsByName(String keyword) {
    log.info("재료 검색 요청 - 키워드: {}", keyword);
    // 키워드로 재료를 검색
    List<Ingredient> ingredients = ingredientRepository.findByNameContaining(keyword);

    // 검색된 재료들을 DTO로 변환하여 리스트에 모음
    List<IngredientSearchResponse> responseList = ingredients.stream()
        .map(IngredientSearchResponse::toDTO) 
        .collect(Collectors.toList());

    log.info("재료 검색 결과 - {}개의 재료가 검색됨", responseList.size());
    return responseList;
  }


  /**
   * 레시피를 저장하는 메소드. 이 메소드는 주어진 레시피와 썸네일 파일을 통해
   * 레시피와 그 재료를 데이터베이스에 저장합니다.
   *
   * @param recipe - 저장할 레시피 정보가 담긴 AdminRecipeRegisterRequest 객체
   * @param thumbnailFile - 레시피 썸네일 이미지 파일 (선택적)
   * @return boolean - 레시피 및 재료 저장이 성공하면 true, 실패하면 false 반환
   */
  @Transactional
  @Override
  public boolean saveRecipe(AdminRecipeRegisterRequest recipe, MultipartFile thumbnailFile) {
    try {
      // 카테고리 조회
      log.info("카테고리 조회 - 카테고리 ID: {}", recipe.getCategoryId());
      Category category = categoryRepository.findById(recipe.getCategoryId())
          .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다: " + recipe.getCategoryId()));

      // 썸네일 파일이 있을 경우 처리
      String thumbnailUrl = null;

      if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
        try {
          // 파일을 저장하고 URL을 생성함.
          log.info("썸네일 파일 업로드 시작 - 파일명: {}", thumbnailFile.getOriginalFilename());
          String savedFileName = fileUploadService.uploadRecipeFile(thumbnailFile);
          if (savedFileName != null) {
            thumbnailUrl = RECIPE_THUMBNAIL_ACCESS_PATH + savedFileName; 
            log.info("썸네일 파일 업로드 완료 - 저장된 파일명: {}", savedFileName);
          }
        } catch (Exception e) {
          throw new RuntimeException("썸네일 파일 저장 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
      }

      // 레시피 저장
      RecipeItem recipeItem = RecipeItem.builder()
          .title(recipe.getTitle())
          .description(recipe.getDescription())
          .category(category)
          .servings(recipe.getServings() != null ? recipe.getServings() : 1)
          .price(recipe.getPrice())
          .thumbnailUrl(thumbnailUrl)
          .build();

      RecipeItem savedRecipe = recipeRepository.save(recipeItem);
      log.info("레시피 저장 완료 - 레시피 ID: {}", savedRecipe.getId());

      // 재료 저장
      for (IngredientDTO ingredientDTO : recipe.getIngredients()) {
        
        // 재료 조회
        log.info("재료 조회 - 재료 ID: {}", ingredientDTO.getIngredientId());

        Ingredient ingredient = ingredientRepository.findById(ingredientDTO.getIngredientId())
          .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 재료 ID입니다: " + ingredientDTO.getIngredientId()));

        // RecipeIngredient 객체 생성 및 저장
        RecipeIngredient recipeIngredient = RecipeIngredient.builder()
            .recipe(savedRecipe)
            .ingredient(ingredient)
            .quantity(ingredientDTO.getQuantity())
            .price((int) (ingredientDTO.getQuantity() * ingredient.getPrice()))
            .build();

        recipeIngredientRepository.save(recipeIngredient);
        log.info("재료 저장 완료 - 재료 ID: {}, 레시피 ID: {}", ingredient.getId(), savedRecipe.getId());
      }

      // 모든 작업이 성공적으로 완료되면 true 반환
      log.info("레시피 및 재료 저장이 성공적으로 완료되었습니다 - 레시피 ID: {}", savedRecipe.getId());
      return true;

    } catch (Exception e) {
      // 에러 발생 시 로그 출력 및 실패 코드 반환
      log.error("레시피 및 재료 저장 중 오류 발생: " + e.getMessage(), e);
      return false;
    }
  }

  @Override
  public List<RecipeIngredientResponse> findIngredientsByRecipeId(Long id) {
      // 먼저 레시피가 존재하는지 확인
      RecipeItem recipe = recipeRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("해당 레시피가 존재하지 않습니다: " + id));

      // 레시피가 존재할 때 재료들을 조회하고 DTO로 변환하여 반환
      List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipeId(id);
      return recipeIngredients.stream()
              .map(RecipeIngredientResponse::toDTO)
              .collect(Collectors.toList());
  }
  
}
