package com.costcook.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.costcook.domain.request.AdminRecipeRegisterRequest;
import com.costcook.domain.response.IngredientSearchResponse;
import com.costcook.domain.response.RecipeIngredientResponse;

public interface AdminService {

  List<IngredientSearchResponse> getIngredientsByName(String keyword);

  boolean saveRecipe(AdminRecipeRegisterRequest recipe, MultipartFile thumbnailFile);

  public List<RecipeIngredientResponse> findIngredientsByRecipeId(Long id);

}
