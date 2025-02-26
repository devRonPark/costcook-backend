package com.costcook.service;

import java.util.List;
import java.util.Map;

import com.costcook.domain.OAuthUserInfo;
import com.costcook.domain.PlatformTypeEnum;
import com.costcook.domain.request.UserUpdateRequest;
import com.costcook.entity.User;

public interface UserService {
	// Provider(kakao 혹은 google) 로부터 사용자 정보(OAuthUserInfo) 추출
	public OAuthUserInfo getOAuthUserInfo(String code, PlatformTypeEnum platformType);
	// 사용자 정보 업데이트(프로필 이미지, 닉네임, 개인정보 수집 및 이용 동의 여부)
	public void updateUserInfo(User user, UserUpdateRequest requestDTO);
	// 사용자 선호 재료 및 기피 재료 업데이트
	public void updateUserTaste(User user, UserUpdateRequest requestDTO);
	// 사용자 선호 재료 및 기피 재료 목록 조회
	public Map<String, List<Long>> getPreferredAndDislikedCategoryIds(User user);
	// 닉네임 중복 확인
	boolean checkNicknameDuplicate(String nickname);
}
