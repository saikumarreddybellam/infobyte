package com.infobyte.task.project.repositories;

import com.infobyte.task.project.entities.PasswordResetToken;
import com.infobyte.task.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    void deleteByUser(User user);
    void deleteByExpiryDateBefore(Instant now);
}