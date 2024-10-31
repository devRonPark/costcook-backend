package com.costcook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.costcook.entity.Ingredient;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

  List<Ingredient> findByNameContaining(String keyword);

  boolean existsByName(String name);

}
