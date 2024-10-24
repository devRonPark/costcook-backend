package com.costcook.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.response.IngredientSearchResponse;
import com.costcook.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

  private final AdminService adminService;

  @GetMapping("/ingredients")
  public ResponseEntity<List<IngredientSearchResponse>> getIngredientList(@RequestParam String keyword) {

    log.info("Keyword received: {}", keyword);

    List<IngredientSearchResponse> responseList = adminService.getIngredientsByName(keyword);

    return ResponseEntity.ok(responseList);
  }

}
