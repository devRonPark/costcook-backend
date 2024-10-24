package com.costcook.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ingredients")
public class Ingredient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;

  // 이름
  @Column(nullable = false, length = 255)
  private String name;

  // 카테고리
  @OneToOne
  @JoinColumn(name = "category_id", nullable = true)
  private Category category;

  // 단위
  @OneToOne
  @JoinColumn(name = "unit_id", nullable = false)
  private Unit unit;

  // 단위당 가격
  @Column(nullable = false)
  @Builder.Default()
  private int price = 0;
  
}
