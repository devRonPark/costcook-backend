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
import com.costcook.service.AdminRecipeService;
import com.costcook.service.FileUploadService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRecipeServiceImpl implements AdminRecipeService {

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
  public List<IngredientSearchResponse> searchIngredientsByName(String keyword) {
    String validKeyword = keyword == null ? "" : keyword;
    log.info("재료 검색 요청 - 키워드: {}", validKeyword);
    // 키워드로 재료를 검색
    List<Ingredient> ingredients = ingredientRepository.findByNameContaining(validKeyword);

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
          .rcpSno(recipe.getRcpSno())
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

  @Transactional
  @Override
  public boolean updateRecipe(
    Long id, 
    AdminRecipeRegisterRequest recipeRequest,
    MultipartFile newThumbnailFile) {
    
    // [1] 기존 레시피 조회
    Recipe recipe = recipeRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("해당 레시피가 존재하지 않습니다: " + id));

    // [2] 썸네일 파일 처리
    String thumbnailUrl = processThumbnailFile(newThumbnailFile, recipeRequest.isThumbnailDeleted(), recipe);

    // [3] 레시피 필드 업데이트 (null이 아닌 필드만 수정)
    updateRecipeFields(recipe, recipeRequest, thumbnailUrl);

    // [4] 재료 업데이트
    updateIngredients(id, recipeRequest.getIngredients(), recipe);

    log.info("레시피 및 재료 수정 완료 - 레시피 ID: {}", recipe.getId());
    return true;
  }


  // [2] 썸네일 파일 처리
  private String processThumbnailFile(MultipartFile newThumbnailFile, boolean thumbnailDeleted, Recipe recipe) {
    
    // 새로운 썸네일 파일이 있을 경우 파일 업데이트
    if (newThumbnailFile != null && !newThumbnailFile.isEmpty()) {
      log.info("새 썸네일 파일 업로드 - 파일명: {}", newThumbnailFile.getOriginalFilename());
      String savedFileName = fileUploadService.uploadRecipeFile(newThumbnailFile);
      return RECIPE_THUMBNAIL_ACCESS_PATH + savedFileName;  // 파일의 서버 경로
    } 

    // 썸네일 삭제 플래그가 설정된 경우, 썸네일 URL을 null로 설정
    else if (thumbnailDeleted) {
      log.info("썸네일 이미지가 삭제됩니다.");
      return null;
    }

    // 기존 썸네일 URL 유지
    return recipe.getThumbnailUrl();
  }


  // [3] 변경된 필드만 update
  private void updateRecipeFields(Recipe recipe, AdminRecipeRegisterRequest request, String thumbnailUrl) {
    
    // 제목이 null이 아닌 경우에만 업데이트
    recipe.setTitle(request.getTitle() != null ? request.getTitle() : recipe.getTitle());
    
    // 고유번호가 null이 아닌 경우에만 업데이트
    recipe.setRcpSno(request.getRcpSno() != null ? request.getRcpSno() : recipe.getRcpSno());
    
    // 설명이 null이 아닌 경우에만 업데이트
    recipe.setDescription(request.getDescription() != null ? request.getDescription() : recipe.getDescription());
    
    // 서빙 수가 null이 아닌 경우에만 업데이트
    recipe.setServings(request.getServings() != null ? request.getServings() : recipe.getServings());

    // 카테고리 ID가 null이 아닌 경우에만 카테고리 업데이트
    if (request.getCategoryId() != null) {
      Category category = categoryRepository.findById(request.getCategoryId())
          .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다: " + request.getCategoryId()));
      recipe.setCategory(category);
    }
    
    // 썸네일 URL은 삭제 요청이 있거나 새 URL이 설정될 때만 업데이트
    if (request.isThumbnailDeleted()) {
      recipe.setThumbnailUrl(null); // 삭제 요청이 있는 경우 URL을 null로 설정
    } else if (thumbnailUrl != null) {
      recipe.setThumbnailUrl(thumbnailUrl); // 새 URL이 있는 경우 업데이트
    }
  }


  // [4] 재료 업데이트 처리
  private void updateIngredients(Long recipeId, List<IngredientDTO> ingredients, Recipe recipe) {
    if (ingredients == null || ingredients.isEmpty()) {
      throw new IllegalArgumentException("레시피에는 최소 한 개 이상의 재료가 필요합니다.");
    }
    
    // [4-1] 기존 재료 목록 가져오기
    Map<Long, RecipeIngredient> existingIngredients = getExistingIngredientsMap(recipeId);
    
    for (IngredientDTO dto : ingredients) {
      validateIngredientDTO(dto);

      // 기존 재료 업데이트 또는 새 재료 추가
      RecipeIngredient existingIngredient = existingIngredients.remove(dto.getIngredientId());
      if (existingIngredient != null) {
        updateExistingIngredient(existingIngredient, dto);
      } else {
        addNewIngredient(dto, recipe);
      }
    }

    // [4-3] 남아있는 기존 재료 삭제
    deleteRemainingIngredients(existingIngredients);
  }

  // [4-1] 기존 재료 목록 가져오기
  private Map<Long, RecipeIngredient> getExistingIngredientsMap(Long recipeId) {
    return recipeIngredientRepository.findByRecipeId(recipeId).stream()
        .collect(Collectors.toMap(ri -> ri.getIngredient().getId(), ri -> ri));
  }

  // [4-2] 재료 유효성 검사
  private void validateIngredientDTO(IngredientDTO dto) {
    if (dto.getIngredientId() == null) {
        throw new IllegalArgumentException("재료 ID가 null일 수 없습니다.");
    }
    if (dto.getQuantity() <= 0) {
        throw new IllegalArgumentException("재료의 양은 0보다 커야 합니다.");
    }
  }

  // [4-3] 기존 재료 업데이트
  private void updateExistingIngredient(RecipeIngredient existingIngredient, IngredientDTO dto) {
    existingIngredient.setQuantity(dto.getQuantity());
    recipeIngredientRepository.save(existingIngredient);
    log.info("기존 재료 업데이트 - 재료 ID: {}", dto.getIngredientId());
  }

  // [4-4] 새로운 재료 추가
  private void addNewIngredient(IngredientDTO dto, Recipe recipe) {
    Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId())
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 재료 ID입니다: " + dto.getIngredientId()));
    RecipeIngredient newIngredient = RecipeIngredient.builder()
        .recipe(recipe)
        .ingredient(ingredient)
        .quantity(dto.getQuantity())
        .build();
    recipeIngredientRepository.save(newIngredient);
    log.info("새로운 재료 추가 - 재료 ID: {}", dto.getIngredientId());
  }

  // [4-5] 남아있는 기존 재료 삭제
  private void deleteRemainingIngredients(Map<Long, RecipeIngredient> existingIngredients) {
    existingIngredients.values().forEach(this::deleteIngredient);
  }

  // [4-6] 재료 삭제
  private void deleteIngredient(RecipeIngredient ingredient) {
    recipeIngredientRepository.delete(ingredient);
    log.info("기존 재료 삭제 - 재료 ID: {}", ingredient.getIngredient().getId());
  }

  
}
