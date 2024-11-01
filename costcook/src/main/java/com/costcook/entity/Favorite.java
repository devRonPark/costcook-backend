package com.costcook.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "favorites")
public class Favorite {
	
	// id(복합키), 회원 식별자, 레시피 식별자, 등록일, 삭제일

	// 복합키
	@EmbeddedId
	private FavoriteId id;
	
	// 회원 식별자
	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// 레시피 식별자
	@ManyToOne
	@MapsId("recipeId")
	@JoinColumn(name = "recipe_id", nullable = false)
	private Recipe recipe;
	
	// 등록일
	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	// 삭제일 : 즐겨찾기 ON, OFF
	@LastModifiedDate
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
	
	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
	
	// Soft delete 메소드 : 즐겨찾기가 생성 -> 삭제 -> 생성시 새 데이터가 생기는게 아니라 update
	public void softDelete() {
		this.deletedAt = LocalDateTime.now();
	}
	
	
	
	
	
	

}
