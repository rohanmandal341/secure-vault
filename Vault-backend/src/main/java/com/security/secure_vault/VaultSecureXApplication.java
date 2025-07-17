package com.security.secure_vault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VaultSecureXApplication {

	public static void main(String[] args) {
		SpringApplication.run(VaultSecureXApplication.class, args);
	}

}
