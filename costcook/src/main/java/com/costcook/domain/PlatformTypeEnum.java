package com.costcook.domain;

public enum PlatformTypeEnum {
	KAKAO("kakao"),
	GOOGLE("google");
	
	String auth;
	
	PlatformTypeEnum(String auth) {
		this.auth = auth;
	}
	
	public String getAuth() {
		return auth;
	}
	
	public static PlatformTypeEnum fromString(String value) {
        for (PlatformTypeEnum authEnum : PlatformTypeEnum.values()) {
            if (authEnum.getAuth().equalsIgnoreCase(value)) {
                return authEnum;
            }
        }
        throw new IllegalArgumentException("적절하지 않은 소셜 로그인 제공자입니다.");
    }
}