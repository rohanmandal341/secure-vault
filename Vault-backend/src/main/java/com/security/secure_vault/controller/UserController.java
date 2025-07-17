package com.security.secure_vault.controller;

import com.security.secure_vault.DTOs.*;
import com.security.secure_vault.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<String> initiate(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.initiateRegistration(request));
    }

    @PostMapping("/register/verify")
    public ResponseEntity<String> verify(@RequestParam String email, @RequestParam String otp) {
        return ResponseEntity.ok(service.verifyOtpRegister(email, otp));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }
    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(service.sendOtpForReset(request));
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(service.resetPassword(
                request.getEmail(), request.getOtp(), request.getNewPassword()));
    }


}
