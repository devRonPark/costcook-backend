package com.costcook.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.costcook.domain.response.IngredientSearchResponse;
import com.costcook.entity.Ingredient;
import com.costcook.repository.IngredientRepository;
import com.costcook.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final IngredientRepository ingredientRepository;

  @Override
  public List<IngredientSearchResponse> getIngredientsByName(String keyword) {
    // 키워드로 재료를 검색
    List<Ingredient> ingredients = ingredientRepository.findByNameContaining(keyword);

    // 검색된 재료들을 DTO로 변환하여 리스트에 모음
    return ingredients.stream()
            .map(IngredientSearchResponse::toDTO) 
            .collect(Collectors.toList());
  }
  
}
