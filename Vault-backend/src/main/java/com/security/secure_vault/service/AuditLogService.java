package com.security.secure_vault.service;

import com.security.secure_vault.model.AuditLog;
import com.security.secure_vault.repository.AuditLogRepository;
import com.security.secure_vault.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository repository;
    private final EmailSenderService emailSender;
    private final IpUtil ipUtil;
    private final HttpServletRequest request;

    public void logAction(String username, String action) {
        AuditLog log = AuditLog.builder()
                .username(username)
                .action(action)
                .ipAddress(ipUtil.extractClientIp(request))
                .timestamp(LocalDateTime.now())
                .build();

        repository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return repository.findAll();
    }

    // Daily report at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailyReportToAdmin() {
        List<AuditLog> logs = repository.findAll();
        if (logs.isEmpty()) return;

        StringBuilder sb = new StringBuilder("📋 Daily Audit Log Report\n\n");
        for (AuditLog log : logs) {
            sb.append("👤 ").append(log.getUsername())
                    .append(" | 🛠️ ").append(log.getAction())
                    .append(" | 📅 ").append(log.getTimestamp())
                    .append(" | 🌐 ").append(log.getIpAddress())
                    .append("\n");
        }

        // Assuming hardcoded admin for now
        emailSender.sendOtp("rohanmandal913@gmail.com", sb.toString());
    }
}
