package com.security.secure_vault.config;

import com.security.secure_vault.service.SharedLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkCleanupScheduler {

    private final SharedLinkService service;

    // Every 15 minutes
    @Scheduled(fixedRate = 15 * 60 * 1000)
    public void cleanupExpiredLinks() {
        service.cleanExpiredLinks();
    }
}
