package com.costcook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.costcook.entity.PreferredIngredient;
import com.costcook.entity.PreferredIngredientId;

@Repository
public interface PreferredIngredientRepository extends JpaRepository<PreferredIngredient, PreferredIngredientId> {
    @Query("SELECT pi.id.categoryId FROM PreferredIngredient pi WHERE pi.user.id = :userId AND pi.deletedAt IS NULL")
    List<Long> findCategoryIdsByUserId(@Param("userId") Long userId);
    List<PreferredIngredient> findByUserId(Long userId); // 사용자 ID로 선호 재료 조회
    boolean existsByUserIdAndCategoryId(Long userId, Long categoryId); // 특정 userId와 categoryId로 선호 재료가 존재하는지 확인
}
