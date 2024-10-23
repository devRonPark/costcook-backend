package com.costcook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.costcook.domain.response.IngredientListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

  @GetMapping("/ingredients")
  public ResponseEntity<IngredientListResponse> getIngredientListByKeyword(@RequestParam String keyword) {
    log.info("Keyword received: {}", keyword);
    return null;
  }


}
