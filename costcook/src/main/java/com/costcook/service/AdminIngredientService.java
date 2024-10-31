package com.costcook.service;

import java.util.List;

import com.costcook.domain.response.AdminIngredientResponse;

public interface AdminIngredientService {

  List<AdminIngredientResponse> getAllIngredients();

  List<AdminIngredientResponse> searchIngredientsByName(String keyword);
  
}
