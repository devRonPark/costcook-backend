package com.costcook.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.costcook.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // ID로 카테고리를 조회하는 메소드
    Optional<Category> findById(Long id);
}
