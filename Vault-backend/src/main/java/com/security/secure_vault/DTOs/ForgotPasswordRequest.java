package com.security.secure_vault.DTOs;


import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
}