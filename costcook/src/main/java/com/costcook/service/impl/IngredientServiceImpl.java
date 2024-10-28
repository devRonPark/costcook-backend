package com.costcook.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.costcook.entity.Category;
import com.costcook.entity.Ingredient;
import com.costcook.entity.Unit;
import com.costcook.repository.CategoryRepository;
import com.costcook.repository.IngredientRepository;
import com.costcook.repository.UnitRepository;
import com.costcook.service.IngredientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {
	
	private final IngredientRepository ingredientRepository;
	private final CategoryRepository categoryRepository;
	private final UnitRepository unitRepository;
	
	// 카테고리, 양 객체의 ID만 찾기
	private Category findCategoryById(Long id) {
		return categoryRepository.findById(id).orElse(null);
	}
	private Unit findUnitById(Long id) {
		return unitRepository.findById(id).orElse(null);
	}
}
