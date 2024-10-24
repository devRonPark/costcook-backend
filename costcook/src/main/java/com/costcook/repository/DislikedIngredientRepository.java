package com.costcook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.costcook.entity.DislikedIngredient;
import com.costcook.entity.DislikedIngredientId;

@Repository
public interface DislikedIngredientRepository extends JpaRepository<DislikedIngredient, DislikedIngredientId> {
    List<DislikedIngredient> findByUserId(Long userId); // 사용자 ID로 기피 재료 조회
    boolean existsByUserIdAndCategoryId(Long userId, Long categoryId); // 특정 userId와 categoryId로 선호 재료가 존재하는지 확인
}
