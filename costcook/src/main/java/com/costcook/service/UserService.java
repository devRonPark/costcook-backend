package com.costcook.service;

import com.costcook.domain.OAuthUserInfo;
import com.costcook.domain.PlatformTypeEnum;
import com.costcook.domain.request.UserUpdateRequest;

public interface UserService {
	// Provider(kakao 혹은 google) 로부터 사용자 정보(OAuthUserInfo) 추출
	public OAuthUserInfo getOAuthUserInfo(String code, PlatformTypeEnum platformType);
	// 사용자 정보 업데이트(프로필 이미지, 닉네임, 개인정보 수집 및 이용 동의 여부)
	public void updateUserInfo(Long userId, UserUpdateRequest requestDTO);
}
