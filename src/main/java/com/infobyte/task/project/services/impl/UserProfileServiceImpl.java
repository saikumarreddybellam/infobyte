package com.infobyte.task.project.services.impl;

import com.infobyte.task.project.dtos.ProfileUpdateRequest;
import com.infobyte.task.project.dtos.UserProfileDto;
import com.infobyte.task.project.entities.User;
import com.infobyte.task.project.repositories.QuizAttemptRepository;
import com.infobyte.task.project.repositories.UserRepository;
import com.infobyte.task.project.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileDto getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        long attemptsCount = quizAttemptRepository.countByUserUsername(username);
        double averageScore = quizAttemptRepository.getAverageScoreByUser(username)
                .orElse(0.0);

        return UserProfileDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .registeredAt(user.getCreatedAt())
                .totalQuizAttempts((int) attemptsCount)
                .averageScore(averageScore)
                .build();
    }

    @Override
    public void updateUserProfile(String username, ProfileUpdateRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update password if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                throw new RuntimeException("Passwords do not match");
            }
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Update email if provided and changed
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }

        userRepository.save(user);
    }
}