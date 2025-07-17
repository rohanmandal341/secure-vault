package com.security.secure_vault.service;

import com.security.secure_vault.exception.ExpiredLinkException;
import com.security.secure_vault.model.FileDocument;
import com.security.secure_vault.model.SharedLink;
import com.security.secure_vault.repository.FileRepository;
import com.security.secure_vault.repository.IpAccessLogRepository;
import com.security.secure_vault.repository.SharedLinkRepository;
import com.security.secure_vault.utils.IpUtil;
import com.security.secure_vault.utils.LinkUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SharedLinkService {
    @Autowired
    private IpAccessLogRepository ipAccessLogRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IpUtil ipUtil;

    private final SharedLinkRepository linkRepository;
    private final FileRepository fileRepository;

    public String generateLink(Long fileId) {
        FileDocument file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        String token = LinkUtil.generateToken();

        SharedLink link = SharedLink.builder()
                .token(token)
                .file(file)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();

        linkRepository.save(link);

        return "http://localhost:1010/api/files/share/" + token;
    }

    public byte[] getFileFromToken(String token) throws IOException {
        SharedLink link = linkRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid link"));

        if (link.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ExpiredLinkException("This shareable link has expired.");
        }

        FileDocument file = link.getFile();
        String email = file.getUploadedBy(); // Assuming `uploadedBy` is the user’s email
        String currentIp = ipUtil.extractClientIp(request);

        // Check if this IP was used before
        boolean isKnownIp = ipAccessLogRepository.findByEmail(email).stream()
                .anyMatch(log -> log.getIpAddress().equals(currentIp));

        // If new IP, save and send alert
        if (!isKnownIp) {
            ipAccessLogRepository.save(
                    com.security.secure_vault.model.IpAccessLog.builder()
                            .email(email)
                            .ipAddress(currentIp)
                            .accessedAt(LocalDateTime.now())
                            .build()
            );

            String body = String.format("""
                ⚠️ Security Alert from VaultSecureX

                Your file was accessed using a new IP address:
                IP: %s
                Time: %s

                If this wasn't you, we recommend securing your account.
                """, currentIp, LocalDateTime.now());

            emailSenderService.sendOtp(email, body);
        }

        // Return file content
        Path path = Paths.get(file.getUploadPath());
        return Files.readAllBytes(path);
    }


    @Transactional
    public void cleanExpiredLinks() {
        linkRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }
}
