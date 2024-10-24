package com.costcook.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.costcook.entity.Category;
import com.costcook.repository.CategoryRepository;
import com.costcook.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryRepository categoryRepository;

	@Override
	@Transactional
	public void insertCategories() {
		Category[] categories = {
				new Category(1L, "채소류", 14L),
				new Category(2L, "쌀", 14L),
				new Category(3L, "닭고기", 14L),
				new Category(4L, "밀가루", 14L),
				new Category(5L, "건어물류", 14L),
				new Category(6L, "콩/견과류", 14L),
				new Category(7L, "곡류", 14L),
				new Category(8L, "해물류", 14L),
				new Category(9L, "가공식품류", 14L),
				new Category(10L, "기타", 14L),
				new Category(11L, "돼지고기", 14L),
				new Category(12L, "달걀/유제품", 14L),
				new Category(13L, "과일류", 14L),
				new Category(14L, "재료", 0L),
				new Category(15L, "찌개", 25L),
				new Category(16L, "퓨전", 25L),
				new Category(17L, "빵", 25L),
				new Category(18L, "메인반찬", 25L),
				new Category(19L, "밑반찬", 25L),
				new Category(20L, "면/만두", 25L),
				new Category(21L, "밥/죽/떡", 25L),
				new Category(22L, "샐러드", 25L),
				new Category(23L, "국/탕", 25L),
				new Category(24L, "양식", 25L),
				new Category(25L, "카테고리", 0L),
				new Category(26L, "간식", 25L),
				new Category(27L, "국/탕", 25L),
				new Category(28L, "소고기", 14L),
				new Category(29L, "버섯류", 14L),
		};
		for (Category category : categories) {
			categoryRepository.save(category);
		}
	}



}
