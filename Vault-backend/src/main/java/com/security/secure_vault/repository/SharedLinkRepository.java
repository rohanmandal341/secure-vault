package com.security.secure_vault.repository;

import com.security.secure_vault.model.SharedLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SharedLinkRepository extends JpaRepository<SharedLink, Long> {
    Optional<SharedLink> findByToken(String token);
    void deleteByExpiresAtBefore(LocalDateTime now);
}
