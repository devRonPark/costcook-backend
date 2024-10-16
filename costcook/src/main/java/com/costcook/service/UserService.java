package com.costcook.service;

import com.costcook.domain.OAuthUserInfo;
import com.costcook.domain.PlatformTypeEnum;

public interface UserService {
	// Provider(kakao 혹은 google) 로부터 사용자 정보(OAuthUserInfo) 추출
	OAuthUserInfo getOAuthUserInfo(String code, PlatformTypeEnum platformType);
}
