package com.security.secure_vault.service;


import com.security.secure_vault.DTOs.ForgotPasswordRequest;
import com.security.secure_vault.DTOs.LoginRequest;
import com.security.secure_vault.DTOs.LoginResponse;
import com.security.secure_vault.DTOs.RegisterRequest;
import com.security.secure_vault.model.User;
import com.security.secure_vault.repository.UserRepository;
import com.security.secure_vault.utils.JwtUtil;
import com.security.secure_vault.utils.OtpStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final OtpStore otpStore;
    private final EmailSenderService senderService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil util;

    public String initiateRegistration(RegisterRequest request){
        if(repository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already registered");
        }
        String otp = String.valueOf(new Random().nextInt(900000)+100000);
        otpStore.saveOtp(request.getEmail(),otp,request);
        senderService.sendOtp(request.getEmail(),otp);
        return "OTP sent to " + request.getEmail();
    }

    public String verifyOtpRegister(String email, String otp) {
        if (!otpStore.verifyOtp(email, otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        RegisterRequest request = otpStore.getPendingUser(email);
        if(request == null){
            throw new RuntimeException("No pending registration found");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        repository.save(user);
        otpStore.remove(email);

        return "User registered successFully";
    }

    public LoginResponse login(LoginRequest request){
        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("user not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        String token = util.generateToken(user.getEmail());
        return new LoginResponse(token);
    }
    public String sendOtpForReset(ForgotPasswordRequest request) {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        // ✅ Use the correct save method for reset
        otpStore.saveOtpForReset(request.getEmail(), otp);

        // ✅ Log to debug if needed
        System.out.println("Sending OTP for password reset: " + otp);

        senderService.sendOtp(request.getEmail(), otp);
        return "OTP sent to " + request.getEmail();
    }




    public String resetPassword(String email, String otp, String newPassword) {
        if (!otpStore.verifyOtp(email, otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        repository.save(user);
        otpStore.remove(email);

        return "Password reset successful";
    }


}
