package com.infobyte.task.project.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserProfileDto {
    private String username;
    private String email;
    private String role;
    private LocalDateTime registeredAt;
    private int totalQuizAttempts;
    private double averageScore;
}