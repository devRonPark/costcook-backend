package com.costcook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;
	
	// 레시피의 ID
	@ManyToOne
	@JoinColumn(name = "recipe_id", nullable = true)
	private RecipeItem recipe;
	
	// 재료의 ID
	@ManyToOne
	@JoinColumn(name = "ingredient_id", nullable = true)
	private Ingredient ingredient;
	
	// 재료의 양
	@Column(nullable = false)
	@Builder.Default()
	private double quantity = 0.0;
	
	// 포함된 재료의 가격
	@Column(nullable = false)
	@Builder.Default()
	private int price = 0;
	
}
