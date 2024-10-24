package com.costcook.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.service.CategoryService;
import com.costcook.service.IngredientService;
import com.costcook.service.RecipeIngredientService;
import com.costcook.service.RecipeService;
import com.costcook.service.UnitService;

import lombok.RequiredArgsConstructor;

/////////////////////////////////////////////////////
// SQL 덤프파일로 대체
//
// DB에 직접 데이터를 넣는 메소드들입니다.
// 포스트맨에서 해당 api 주소로 Send하면 됩니다.
//////////////////////////////////////////////////////

@RestController
@RequiredArgsConstructor
@RequestMapping("/insert")
public class DBInsertController {

	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final CategoryService categoryService;
	private final UnitService unitService;
	private final RecipeIngredientService recipeIngredientService;
	

    // 레시피 (RecipeItem)
    @PostMapping("/recipes")
    public String insertRecipes() {
        recipeService.insertRecipes();
        return "레시피 DB 등록 완료";
    };

    // 재료 (Ingredient)
    @PostMapping("/ingredients")
    public String insertIngredients() {
        ingredientService.insertIngredients();
        return "재료 DB 등록 완료";
    }

    // 카테고리 (Category)
    @PostMapping("/categories")
    public String insertCategories() {
    	categoryService.insertCategories();
    	return "카테고리 DB 등록 완료";
    }

    // 양 (Unit)
    @PostMapping("/units")
    public String insertUnits() {
        unitService.insertUnits();
        return "양(unit) DB 등록 완료";
    }

    // 레시피 당 재료 (RecipeIngredient)
    @PostMapping("/recipeIngredients")
    public String insertRecipeIngredients() {
        recipeIngredientService.insertRecipeIngredients();
        return "레시피 당 재료 DB 등록 완료";
    }
	
	
	
	
	
	
}
