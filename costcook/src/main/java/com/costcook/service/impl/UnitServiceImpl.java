package com.costcook.service.impl;

import org.springframework.stereotype.Service;

import com.costcook.entity.Unit;
import com.costcook.repository.UnitRepository;
import com.costcook.service.UnitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

	private final UnitRepository unitRepository;
	
	@Override
	public void insertUnits() {
		Unit[] units = {
				new Unit(1L, "T"),
				new Unit(2L, "t"),
				new Unit(3L, "g"),
				new Unit(4L, "ml"),
				new Unit(5L, "개"),
				new Unit(6L, "대"),
				new Unit(7L, "모"),
				new Unit(8L, "알"),
				new Unit(9L, "줌"),
				new Unit(10L, "공기"),
				new Unit(11L, "컵"),
				new Unit(12L, "쪽"),
				new Unit(13L, "캔"),
				new Unit(14L, "팩"),
				new Unit(15L, "조각"),
				new Unit(16L, "포기"),
				new Unit(17L, "봉지"),
				new Unit(18L, "장"),
				new Unit(19L, "마리"),
				new Unit(20L, "통"),
		};
		for (Unit unit : units) {
			unitRepository.save(unit);
		}
	}

}
