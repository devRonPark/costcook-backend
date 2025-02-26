package com.costcook.service;

import java.util.List;

import com.costcook.domain.request.AdminIngredientRegisterRequest;
import com.costcook.domain.response.AdminIngredientResponse;

public interface AdminIngredientService {

  List<AdminIngredientResponse> getAllIngredients();

  List<AdminIngredientResponse> searchIngredientsByName(String keyword);

  boolean isIngredientDuplicate(String name);

  boolean saveIngredient(AdminIngredientRegisterRequest ingredient);

  boolean updateIngredient(Long ingredientId, AdminIngredientRegisterRequest ingredient);

  boolean deleteIngredient(Long ingredientId);
  
}
