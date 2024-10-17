package com.costcook.service.impl;

import org.springframework.stereotype.Service;
import com.costcook.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	private final JavaMailSender mailSender;

	@Override
	public void sendVerificationCode(String email, String verificationCode) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
        message.setSubject("인증 코드");
        message.setText("인증 코드는: " + verificationCode);
        mailSender.send(message);
	}

}
