package com.costcook.service;

import java.util.List;

import com.costcook.domain.response.IngredientSearchResponse;

public interface AdminService {

  List<IngredientSearchResponse> getIngredientsByName(String keyword);

}
