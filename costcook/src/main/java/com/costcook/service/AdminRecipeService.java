package com.costcook.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.costcook.domain.request.AdminRecipeRegisterRequest;
import com.costcook.domain.response.RecipeIngredientResponse;

public interface AdminRecipeService {

  boolean saveRecipe(AdminRecipeRegisterRequest recipe, MultipartFile thumbnailFile);

  public List<RecipeIngredientResponse> findIngredientsByRecipeId(Long id);

  boolean updateRecipe(Long id, AdminRecipeRegisterRequest recipe, MultipartFile thumbnailFile);

  void deleteRecipe(Long id);

  boolean isRecipeDuplicate(String name);

}
