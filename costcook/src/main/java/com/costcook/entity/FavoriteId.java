package com.costcook.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 즐겨찾기 테이블의 복합키

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteId implements Serializable {

	private Long userId;
	private Long recipeId;
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FavoriteId)) return false;
		FavoriteId that = (FavoriteId) o;
		return Objects.equals(userId, that.userId) &&
			   Objects.equals(recipeId, that.recipeId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(userId, recipeId);
	}
	
	
}
