package com.costcook.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUserInfo {
	String socialKey; 
	String email;
	String name;
	PlatformTypeEnum provider;
	
	public boolean isAbleToLogin() {
		if (this.getSocialKey() != null && this.getEmail() != null && this.getName() != null && this.getProvider() != null) {
			return true;
	    }
		return false;
	}
}