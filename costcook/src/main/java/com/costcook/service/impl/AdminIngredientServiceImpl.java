package com.costcook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.costcook.domain.response.AdminIngredientResponse;
import com.costcook.entity.Ingredient;
import com.costcook.repository.IngredientRepository;
import com.costcook.service.AdminIngredientService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminIngredientServiceImpl implements AdminIngredientService {

  private final IngredientRepository ingredientRepository;

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

}
