package com.infobyte.task.project.config;

import com.infobyte.task.project.repositories.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@Configuration
@EnableScheduling
public class TokenCleanupConfig {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupExpiredTokens() {
        tokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}