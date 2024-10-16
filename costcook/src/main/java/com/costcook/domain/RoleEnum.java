package com.costcook.domain;

public enum RoleEnum {
	ROLE_USER("ROLE_USER"),
	ROLE_ADMIN("ROLE_ADMIN");
	
	String role;
	
    // 생성자
	RoleEnum(String role) {
		this.role = role;
	}
	
    // Getter
	public String getRole() {
		return role;
	}
}
