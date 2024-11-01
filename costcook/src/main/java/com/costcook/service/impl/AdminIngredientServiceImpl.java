package com.costcook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.costcook.domain.request.AdminIngredientRegisterRequest;
import com.costcook.domain.response.AdminIngredientResponse;
import com.costcook.entity.Category;
import com.costcook.entity.Ingredient;
import com.costcook.entity.Unit;
import com.costcook.repository.CategoryRepository;
import com.costcook.repository.IngredientRepository;
import com.costcook.repository.UnitRepository;
import com.costcook.service.AdminIngredientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminIngredientServiceImpl implements AdminIngredientService {

  private final IngredientRepository ingredientRepository;
  private final CategoryRepository categoryRepository;
  private final UnitRepository unitRepository;

  @Override
  public List<AdminIngredientResponse> getAllIngredients() {
    List<Ingredient> ingredients = ingredientRepository.findAll();
    return ingredients.stream()
            .map(AdminIngredientResponse::toDTO)
            .collect(Collectors.toList());
  }

  /**
   * 주어진 키워드를 사용하여 재료를 검색합니다.
   * 
   * @param keyword - 검색할 재료의 키워드
   * @return List<IngredientSearchResponse> - 검색된 재료 정보를 담은 DTO 리스트
   */
  @Override
  public List<AdminIngredientResponse> searchIngredientsByName(String keyword) {
    String validKeyword = keyword == null ? "" : keyword;
    log.info("재료 검색 요청 - 키워드: {}", validKeyword);
    // 키워드로 재료를 검색
    List<Ingredient> ingredients = ingredientRepository.findByNameContaining(validKeyword);

    // 검색된 재료들을 DTO로 변환하여 리스트에 모음
    List<AdminIngredientResponse> responseList = ingredients.stream()
        .map(AdminIngredientResponse::toDTO) 
        .collect(Collectors.toList());

    log.info("재료 검색 결과 - {}개의 재료가 검색됨", responseList.size());
    return responseList;
  }

  @Override
  public boolean isIngredientDuplicate(String name) {
    return ingredientRepository.existsByName(name);
  }

  @Override
  public boolean saveIngredient(AdminIngredientRegisterRequest request) {
    try {
      // 카테고리 
      log.info("카테고리 조회 - 카테고리 ID: {}", request.getCategoryId());
      Category category = categoryRepository.findById(request.getCategoryId())
        .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다: " + request.getCategoryId()));

      // 단위
      log.info("단위 조회 - 단위 ID: {}", request.getUnitId());
      Unit unit = unitRepository.findById(request.getUnitId())
        .orElseThrow(() -> new IllegalArgumentException("해당 단위가 존재하지 않습니다: " + request.getUnitId()));

      Ingredient ingredientDto = Ingredient.builder()
                                  .name(request.getName())
                                  .category(category)
                                  .unit(unit)
                                  .price(request.getPrice())
                                  .build();

      ingredientRepository.save(ingredientDto);

      log.info("재료 저장이 성공적으로 완료되었습니다 - 재료 ID: {}", ingredientDto.getId());
      return true;

    } catch (Exception e) {
      // 에러 발생 시 로그 출력 및 실패 코드 반환
      log.error("재료 저장 중 오류 발생: " + e.getMessage(), e);
      return false;
    }
  }


  @Override
  public boolean updateIngredient(Long ingredientId, AdminIngredientRegisterRequest request) {
    try {
      Ingredient ingredient = ingredientRepository.findById(ingredientId)
        .orElseThrow(() -> new IllegalArgumentException("해당 레시피가 존재하지 않습니다: " + ingredientId));

      // 카테고리 
      Long categoryId = request.getCategoryId();

      if(categoryId != null && categoryId > 0) {
        log.info("카테고리 조회 - 카테고리 ID: {}", categoryId);
        Category category = categoryRepository.findById(categoryId)
          .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다: " + categoryId));
        ingredient.setCategory(category);
      } 
      
      // 단위
      Long unitId = request.getUnitId();

      if(unitId != null && unitId > 0) {
        log.info("단위 조회 - 단위 ID: {}", unitId);
        Unit unit = unitRepository.findById(unitId)
          .orElseThrow(() -> new IllegalArgumentException("해당 단위가 존재하지 않습니다: " + unitId));
        ingredient.setUnit(unit);
      }

      // 단위 가격
      Integer price = request.getPrice();

      if(price != null && price >= 0) {
        ingredient.setPrice(price);
      }

      ingredientRepository.save(ingredient);  
      log.info("재료 수정 완료 - 재료 ID: {}", ingredientId);
      return true;
    } catch(Exception e) {
      // 예외가 발생하면 로그를 기록하고 false 반환
      log.error("재료 수정 중 오류 발생 - 재료 ID: {}, 오류: {}", ingredientId, e.getMessage());
      return false;
    }
  }

}
