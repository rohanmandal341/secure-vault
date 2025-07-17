package com.security.secure_vault.exception;

public class ExpiredLinkException extends RuntimeException {
    public ExpiredLinkException(String message) {
        super(message);
    }
}
