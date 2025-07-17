package com.security.secure_vault.DTOs;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
