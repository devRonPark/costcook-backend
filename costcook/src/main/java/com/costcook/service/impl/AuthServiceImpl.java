package com.costcook.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.costcook.domain.PlatformTypeEnum;
import com.costcook.domain.request.SignUpOrLoginRequest;
import com.costcook.domain.response.SignUpOrLoginResponse;
import com.costcook.entity.DislikedIngredient;
import com.costcook.entity.PreferredIngredient;
import com.costcook.entity.SocialAccount;
import com.costcook.entity.User;
import com.costcook.exceptions.InvalidProviderException;
import com.costcook.exceptions.MissingFieldException;
import com.costcook.repository.DislikedIngredientRepository;
import com.costcook.repository.PreferredIngredientRepository;
import com.costcook.repository.SocialAccountRepository;
import com.costcook.repository.UserRepository;
import com.costcook.security.JwtProvider;
import com.costcook.service.AuthService;
import com.costcook.util.TokenUtils;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final SocialAccountRepository socialAccountRepository;
    private final PreferredIngredientRepository preferredIngredientRepository;
    private final DislikedIngredientRepository dislikedIngredientRepository;
    private final TokenUtils tokenUtils;
    private final JwtProvider jwtProvider;
    
    /**
     * 주어진 소셜 키와 공급자를 사용하여 해당 사용자의 이메일을 조회합니다.
     *
     * 1. 소셜 계정(social_accounts) 테이블에서 소셜 키와 공급자를 기준으로 사용자 정보를 검색합니다.
     * 2. 사용자가 존재할 경우, 해당 사용자의 이메일을 리턴합니다.
     *
     * @param socialKey - 소셜 계정의 고유 키
     * @param provider - 소셜 공급자 유형 (Kakao, Google 등)
     * @return Optional<String> - 사용자의 이메일이 존재할 경우 해당 이메일, 그렇지 않으면 Optional.empty() 반환
     */
    @Override
    public Optional<String> getEmailFromSocialAccount(String socialKey, PlatformTypeEnum provider) {
        // social_accounts 테이블에서 socialKey와 provider를 기준으로 사용자 조회
        Optional<SocialAccount> socialAccountOpt = socialAccountRepository.findBySocialKeyAndProvider(socialKey, provider);
        
        if (socialAccountOpt.isPresent()) {
            // 사용자 ID를 가져옴
            User user = socialAccountOpt.get().getUser();
            return Optional.of(user.getEmail());
        }
        
        return Optional.empty(); // 사용자를 찾지 못한 경우
    }

    @Transactional
    @Override
    public SignUpOrLoginResponse signUpOrLogin(SignUpOrLoginRequest request, HttpServletResponse response) {
        try {
            // 1. Request DTO 유효성 검증
            validateSignUpOrLoginRequest(request);
    
            // 2. email 필드를 기준으로 회원 가입 여부 확인
            // 이 부분에서 에러 발생한다고 예상됨. 원인: preferredIngredients 필드 및 dislikedIngredients 필드에 대해 즉시 로딩 지원으로 인해서.
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseGet(() -> registerNewUser(request));
            // 사용자 프로필 업데이트 여부: 닉네임, 프로필 이미지, 기피 재료, 선호 재료 중 하나라도 설정되어 있지 않다면 false
            List<DislikedIngredient> dislikedIngredients = dislikedIngredientRepository.findByUserId(user.getId());
            List<PreferredIngredient> preferredIngredients = preferredIngredientRepository.findByUserId(user.getId());
            boolean isUserProfileUpdated = user.getNickname() != null && user.getProfileUrl() != null && !dislikedIngredients.isEmpty() && !preferredIngredients.isEmpty();
    
            // 2-2. email 을 기준으로 이미 회원가입한 회원이지만, social_accounts 테이블 조회(provider, socialKey) 없는 경우 registerNewUser(request) 호출 필요.
            log.info("findBySocialKeyAndProvider 호출");        
            Optional<SocialAccount> socialAccount = socialAccountRepository.findBySocialKeyAndProvider(request.getSocialKey(), PlatformTypeEnum.fromString(request.getProvider()));
            log.info("{}", socialAccount.toString());        
            if (!socialAccount.isPresent() && request.getProvider() != null && request.getSocialKey() != null) {
                // 소셜 계정 정보가 없다면 카카오 계정 정보를 등록
                registerSocialAccount(user, request.getProvider(), request.getSocialKey());
                log.info("소셜 계정 정보가 등록되었습니다: {} - {}", request.getProvider(), request.getSocialKey());
            }
    
    
            // 3. 액세스 토큰, 리프레시 토큰 발급
            Map<String, String> tokenMap = tokenUtils.generateToken(user);
            String accessToken = tokenMap.get("accessToken");
            String refreshToken = tokenMap.get("refreshToken");
            log.info("새로 발급된 액세스 토큰: {}", accessToken);
            log.info("새로 발급된 리프레시 토큰: {}", refreshToken);
            
            // 4. 발급된 리프레시 토큰 사용자 테이블에 저장
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
            
            // 5. 쿠키에 생성된 리프레시 토큰과 액세스 토큰을 담아 응답
            tokenUtils.setRefreshTokenCookie(response, refreshToken);
            tokenUtils.setAccessTokenCookie(response, accessToken);
            
            // SignUpOrLoginResponse 객체 생성
            boolean isNewUser = user.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(1)); // 1분 이내에 생성된 경우 신규 사용자로 간주
            SignUpOrLoginResponse signUpOrLoginResponse = SignUpOrLoginResponse.builder()
                    .message(isNewUser ? "회원가입 후 로그인이 완료되었습니다." : "로그인에 성공했습니다.")
                    .accessToken(accessToken)
                    .isNewUser(isNewUser)
                    .isUserProfileUpdated(isUserProfileUpdated)
                    .build();
            
            return signUpOrLoginResponse;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}",e.getMessage());
            throw e;
        }
    }

    // SignUpOrLoginRequest의 필수 필드를 검증
    private void validateSignUpOrLoginRequest(SignUpOrLoginRequest request) {
        log.info(request.toString());
        if (request.getSocialKey() == null || request.getEmail() == null || request.getProvider() == null) {
            throw new MissingFieldException();
        }

        if (!isValidProvider(request.getProvider())) {
            throw new InvalidProviderException("적절하지 않은 소셜 로그인 제공자입니다.");
        }
    }

    // 지원되는 소셜 로그인 제공자인지 확인
    private boolean isValidProvider(String provider) {
        return provider.equals("kakao") || provider.equals("google");
    }
    
    // 신규 사용자 등록 및 소셜 계정 정보 저장
    private User registerNewUser(SignUpOrLoginRequest request) {
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        // 추가적인 사용자 정보 설정...
        
        // 신규 사용자 저장
        User savedUser = userRepository.save(newUser);

        // 소셜 계정 정보 저장
        SocialAccount socialAccount = SocialAccount
                .builder()
                .provider(PlatformTypeEnum.fromString(request.getProvider()))
                .socialKey(request.getSocialKey())
                .user(savedUser)
                .build();
        socialAccountRepository.save(socialAccount);

        return savedUser;
    }

    // 소셜 계정 등록 메소드
    private void registerSocialAccount(User user, String provider, String socialKey) {
    	log.info("{}", PlatformTypeEnum.fromString(provider));
    	log.info("--------");
        SocialAccount newSocialAccount = SocialAccount
                    .builder()
                    .provider(PlatformTypeEnum.fromString(provider))
                    .socialKey(socialKey)
                    .user(user)
                    .build();
        socialAccountRepository.save(newSocialAccount);
    }

    // 리프레시 토큰을 검증하고 새로운 액세스 토큰을 발급하는 메서드
    @Override
    public String refreshAccessToken(String refreshToken) {
        // 1. 리프레시 토큰 유효성 검증
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 2. 리프레시 토큰에서 사용자 정보 추출
        String userEmail = jwtProvider.getUserEmailByToken(refreshToken);
        User user = (User) userDetailsService.loadUserByUsername(userEmail);
        log.info("{}", user.toString());
        if (user == null) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 3. 새로운 액세스 토큰 생성
        String newAccessToken = jwtProvider.generateAccessToken(user);

        // 4. 새로운 액세스 토큰 반환
        return newAccessToken;
    }
}
