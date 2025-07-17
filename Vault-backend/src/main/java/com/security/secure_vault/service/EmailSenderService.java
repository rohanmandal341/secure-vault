package com.security.secure_vault.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender sender;

    public void sendOtp(String toEmail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("rohanmandal7789999@gmail.com");
        message.setTo(toEmail);
        message.setSubject("VaultSecureX OTP Verification");
        message.setText("Your OTP is: " + otp);
        sender.send(message);

        System.out.println("✅ OTP sent to email: " + toEmail);
    }
}
