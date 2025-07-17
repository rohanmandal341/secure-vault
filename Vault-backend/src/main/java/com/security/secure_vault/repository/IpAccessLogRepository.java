package com.security.secure_vault.repository;

import com.security.secure_vault.model.IpAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IpAccessLogRepository extends JpaRepository<IpAccessLog, Long> {
    List<IpAccessLog> findByEmail(String email);
}
