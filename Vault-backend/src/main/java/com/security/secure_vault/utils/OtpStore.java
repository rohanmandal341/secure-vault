package com.security.secure_vault.utils;

import com.security.secure_vault.DTOs.RegisterRequest;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStore {

    private final Map<String, String> otpMap = new ConcurrentHashMap<>();
    private final Map<String, RegisterRequest> pendingUser = new ConcurrentHashMap<>();

    // For registration
    public void saveOtp(String email, String otp, RegisterRequest request){
        otpMap.put(email, otp);
        pendingUser.put(email, request);  // Used only in registration
    }

    // For password reset - no request, just OTP
    public void saveOtpForReset(String email, String otp){
        otpMap.put(email, otp);
    }

    public boolean verifyOtp(String email, String otp){
        return otpMap.containsKey(email) && otpMap.get(email).equals(otp);
    }

    public RegisterRequest getPendingUser(String email){
        return pendingUser.get(email);
    }

    public void remove(String email){
        otpMap.remove(email);
        pendingUser.remove(email);
    }
}
