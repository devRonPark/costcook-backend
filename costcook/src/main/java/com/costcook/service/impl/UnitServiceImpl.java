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
}
