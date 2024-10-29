package com.costcook.service.impl;

import java.util.List;
import java.util.Map;
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
import com.costcook.entity.Recipe;
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
  private final String RECIPE_THUMBNAIL_ACCESS_PATH = "/img/recipe/";

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
      Recipe recipeItem = Recipe.builder()
          .title(recipe.getTitle())
          .description(recipe.getDescription())
          .category(category)
          .servings(recipe.getServings() != null ? recipe.getServings() : 1)
         // .price(recipe.getPrice())
          .thumbnailUrl(thumbnailUrl)
          .build();

      Recipe savedRecipe = recipeRepository.save(recipeItem);
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
            // .price((int) (ingredientDTO.getQuantity() * ingredient.getPrice()))
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
      Recipe recipe = recipeRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("해당 레시피가 존재하지 않습니다: " + id));

      // 레시피가 존재할 때 재료들을 조회하고 DTO로 변환하여 반환
      List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByRecipeId(id);
      return recipeIngredients.stream()
              .map(RecipeIngredientResponse::toDTO)
              .collect(Collectors.toList());
  }


  @Transactional
  @Override
  public boolean updateRecipe(Long id, AdminRecipeRegisterRequest recipeRequest, MultipartFile thumbnailFile) {
      try {
          // 레시피 ID와 레시피 요청 데이터의 유효성 검증
          if (id == null || recipeRequest == null) {
              throw new IllegalArgumentException("레시피 ID나 요청 데이터가 null일 수 없습니다.");
          }

          // 기존 레시피 조회
          log.info("레시피 조회 - 레시피 ID: {}", id);
          Recipe recipe = recipeRepository.findById(id)
              .orElseThrow(() -> new IllegalArgumentException("해당 레시피가 존재하지 않습니다: " + id));

          // 카테고리 조회
          log.info("카테고리 조회 - 카테고리 ID: {}", recipeRequest.getCategoryId());
          Category category = categoryRepository.findById(recipeRequest.getCategoryId())
              .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다: " + recipeRequest.getCategoryId()));

          // 썸네일 파일 처리
          String thumbnailUrl = recipe.getThumbnailUrl();
          if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
              log.info("새 썸네일 파일 업로드 시작 - 파일명: {}", thumbnailFile.getOriginalFilename());
              String savedFileName = fileUploadService.uploadRecipeFile(thumbnailFile);
              thumbnailUrl = RECIPE_THUMBNAIL_ACCESS_PATH + savedFileName;
              log.info("썸네일 파일 업로드 완료 - 저장된 파일명: {}", savedFileName);
          }

          // 레시피 정보 업데이트
          recipe.setTitle(recipeRequest.getTitle());
          recipe.setDescription(recipeRequest.getDescription());
          recipe.setCategory(category);
          recipe.setServings(recipeRequest.getServings() != null ? recipeRequest.getServings() : 1);
          // recipe.setPrice(recipeRequest.getPrice());
          recipe.setThumbnailUrl(thumbnailUrl);

          // 레시피 저장 (업데이트)
          recipeRepository.save(recipe);
          log.info("레시피 수정 완료 - 레시피 ID: {}", recipe.getId());

          // 기존 재료 목록을 Map으로 변환 (ingredientId -> RecipeIngredient)
          List<RecipeIngredient> existingIngredients = recipeIngredientRepository.findByRecipeId(id);
          Map<Long, RecipeIngredient> existingIngredientMap = existingIngredients.stream()
              .collect(Collectors.toMap(ri -> ri.getIngredient().getId(), ri -> ri));

          // 요청된 재료 목록의 유효성 검증
          if (recipeRequest.getIngredients() == null || recipeRequest.getIngredients().isEmpty()) {
              throw new IllegalArgumentException("레시피에 최소 한 개 이상의 재료가 필요합니다.");
          }

          // 요청된 재료를 반복하며 업데이트 또는 새로 추가
          for (IngredientDTO ingredientDTO : recipeRequest.getIngredients()) {
              if (ingredientDTO.getIngredientId() == null) {
                  throw new IllegalArgumentException("재료 ID가 null일 수 없습니다.");
              }
              Long ingredientId = ingredientDTO.getIngredientId();
              RecipeIngredient existingIngredient = existingIngredientMap.get(ingredientId);

              if (existingIngredient != null) {
                  // 기존 재료 업데이트
                  log.info("기존 재료 업데이트 - 재료 ID: {}", ingredientId);
                  existingIngredient.setQuantity(ingredientDTO.getQuantity());
                  // existingIngredient.setPrice((int) (ingredientDTO.getQuantity() * existingIngredient.getIngredient().getPrice()));
                  log.info("기존 재료 업데이트 완료 - 재료 ID: {}, 레시피 ID: {}", ingredientId, recipe.getId());
              } else {
                  // 새로운 재료 추가
                  log.info("새로운 재료 추가 - 재료 ID: {}", ingredientId);
                  Ingredient ingredient = ingredientRepository.findById(ingredientId)
                      .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 재료 ID입니다: " + ingredientId));

                  RecipeIngredient newRecipeIngredient = RecipeIngredient.builder()
                      .recipe(recipe)
                      .ingredient(ingredient)
                      .quantity(ingredientDTO.getQuantity())
                      // .price((int) (ingredientDTO.getQuantity() * ingredient.getPrice()))
                      .build();

                  recipeIngredientRepository.save(newRecipeIngredient);
                  log.info("새로운 재료 추가 완료 - 재료 ID: {}, 레시피 ID: {}", ingredientId, recipe.getId());
              }

              // 업데이트된 재료는 삭제 후보 목록에서 제거
              existingIngredientMap.remove(ingredientId);
          }

          // 요청에 포함되지 않은 기존 재료 삭제
          for (RecipeIngredient ingredientToDelete : existingIngredientMap.values()) {
              recipeIngredientRepository.delete(ingredientToDelete);
              log.info("기존 재료 삭제 완료 - 재료 ID: {}, 레시피 ID: {}", ingredientToDelete.getIngredient().getId(), recipe.getId());
          }

          log.info("레시피 및 재료 수정이 성공적으로 완료되었습니다 - 레시피 ID: {}", recipe.getId());
          return true;

      } catch (IllegalArgumentException e) {
          log.error("유효하지 않은 데이터로 인해 업데이트 실패: {}", e.getMessage());
          return false;
      } catch (Exception e) {
          log.error("레시피 및 재료 수정 중 오류 발생: " + e.getMessage(), e);
          throw new RuntimeException("레시피 수정 중 예기치 못한 오류가 발생했습니다.", e);
      }
  }


  @Transactional
  @Override
  public void deleteRecipe(Long id) {
    try {
      // 레시피 존재 여부 확인
      Recipe recipe = recipeRepository.findById(id)
          .orElseThrow(() -> new IllegalArgumentException("해당 레시피가 존재하지 않습니다. ID: " + id));

      // 관련된 재료 삭제
      log.info("레시피에 연결된 재료 삭제 시작 - 레시피 ID: {}", id);
      recipeIngredientRepository.deleteByRecipeId(id);
      log.info("레시피에 연결된 재료 삭제 완료 - 레시피 ID: {}", id);

      // 레시피 삭제
      recipeRepository.deleteById(id);
      log.info("레시피가 성공적으로 삭제되었습니다. ID: {}", id);

    } catch (IllegalArgumentException e) {
      log.warn("삭제하려는 레시피가 존재하지 않습니다: {}", e.getMessage());
      throw e;
    } catch (Exception e) {
      log.error("레시피 삭제 중 오류 발생: " + e.getMessage(), e);
      throw new RuntimeException("레시피 삭제 중 예기치 못한 오류가 발생했습니다.", e);
    }
  }
  
}
