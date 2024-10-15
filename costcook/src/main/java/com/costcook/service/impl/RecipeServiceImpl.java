package com.costcook.service.impl;

import org.springframework.stereotype.Service;

import com.costcook.repository.RecipeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl {
	
	private final RecipeRepository recipeRepository;
	

}
