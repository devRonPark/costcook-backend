package com.costcook.service.impl;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.costcook.domain.RoleEnum;
import com.costcook.entity.User;
import com.costcook.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	
	@Override
	public User loadUserByUsername(String email) throws UsernameNotFoundException {
		log.info("[loadUserByUsername] 사용자 이메일 조회. username: {}", email);
		User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("없는 이메일"));

    // 관리자인 경우 비밀번호가 반드시 필요하므로 null 체크
    if (user.getRole() == RoleEnum.ROLE_ADMIN && user.getPassword() == null) {
      log.error("[loadUserByUsername] 관리자 계정의 비밀번호가 설정되어 있지 않음: {}", email);
      throw new IllegalStateException("관리자의 비밀번호가 설정되어 있지 않습니다.");
    }
    
		return user;
	}
	
}
