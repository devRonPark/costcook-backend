package com.costcook.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.costcook.entity.EmailVerification;
import com.costcook.repository.EmailVerificationRepository;
import com.costcook.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private final JavaMailSender mailSender;
	private final EmailVerificationRepository emailVerificationRepository;

	@Transactional
	@Override
	public void sendVerificationCode(String email, String verificationCode) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("CostCook 인증 코드 안내");

		String emailBody = "안녕하세요!\n\n" + "CostCook에 가입해 주셔서 감사합니다! 🎉\n\n"
				+ "1주일 예산에 맞춤형 레시피를 추천해 드리는 서비스를 이용하시려면 아래의 인증 코드를 입력해 주세요:\n\n" + "🔑 **인증 코드:** " + verificationCode
				+ "\n\n" + "이 인증 코드는 서비스 가입을 완료하는 데 필요하며, 한 번만 사용 가능합니다.\n" + "코드를 입력한 후, 레시피 추천 서비스를 즐기세요! 🍽️\n\n"
				+ "감사합니다!\n\n" + "CostCook 팀 드림";

		message.setText(emailBody);
		mailSender.send(message);

		// 인증번호 저장
		EmailVerification emailVerification = EmailVerification.builder().userEmail(email)
				.verificationCode(verificationCode).build();

		emailVerificationRepository.save(emailVerification);
	}
	
	public boolean verifyCode(String email, String verificationCode) {
		log.info("verifyCode 메소드 실행 시작");
        EmailVerification verification = emailVerificationRepository.findTopByUserEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));
        log.info("verification 정보: {}", verification.toString());
        
        // 인증 코드 만료 확인
        if (verification.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("인증 코드가 만료되었습니다.");
        }

        // 인증 코드 일치 여부 확인
        log.info("인증 코드 일치여부 확인: {}", verification.getVerificationCode().equals(verificationCode));
        return verification.getVerificationCode().equals(verificationCode);
    }

}
