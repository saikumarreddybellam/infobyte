package com.infobyte.task.project.services.impl;

import com.infobyte.task.project.dtos.LoginRequest;
import com.infobyte.task.project.dtos.LoginResponse;
import com.infobyte.task.project.dtos.PasswordResetRequest;
import com.infobyte.task.project.dtos.RegisterRequest;
import com.infobyte.task.project.entities.PasswordResetToken;
import com.infobyte.task.project.entities.User;
import com.infobyte.task.project.exceptions.EmailSendingException;
import com.infobyte.task.project.exceptions.UserNotFoundException;
import com.infobyte.task.project.repositories.PasswordResetTokenRepository;
import com.infobyte.task.project.repositories.UserRepository;
import com.infobyte.task.project.services.AuthService;
import com.infobyte.task.project.utility.JwtTokenService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final Environment env;

    @Override
    public void register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setEmail(request.getEmail());

        try {
            userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error registering user", e);
            throw new RuntimeException("Registration failed");
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            String token = jwtTokenService.generateToken(user);
            return new LoginResponse(token, user.getUsername(), user.getRole());
        } catch (Exception e) {
            logger.error("Login error for user: " + request.getUsername(), e);
            throw new RuntimeException("Login failed");
        }
    }

    @Override
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        tokenRepository.deleteByUser(user);

        PasswordResetToken resetToken = new PasswordResetToken(user);
        tokenRepository.save(resetToken);

        sendPasswordResetEmail(user, resetToken.getToken());
    }

    @Override
    public void resetPassword(PasswordResetRequest request) {
        PasswordResetToken token = tokenRepository.findByToken(request.getToken());
        if (token == null) {
            throw new RuntimeException("Invalid token");
        }

        if (token.isExpired()) {
            tokenRepository.delete(token);
            throw new RuntimeException("Token has expired");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        tokenRepository.delete(token);
    }

    @Override
    @Transactional
    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        PasswordResetToken resetToken = new PasswordResetToken(user);
        tokenRepository.deleteByUser(user); // Remove any existing tokens
        tokenRepository.save(resetToken);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Password Reset Request");
            helper.setText(buildEmailContent(user.getUsername(), resetToken.getToken()), true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new EmailSendingException("Failed to send password reset email", e);
        }
    }

    private String buildEmailContent(String username, String token) {
        String resetUrl = env.getProperty("app.base-url") + "/reset-password?token=" + token;

        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><style>body{font-family:Arial,sans-serif}</style></head>" +
                "<body>" +
                "<h2>Password Reset</h2>" +
                "<p>Hello " + username + ",</p>" +
                "<p>You requested to reset your password. Click the link below to proceed:</p>" +
                "<p><a href=\"" + resetUrl + "\">Reset Password</a></p>" +
                "<p>This link will expire in 24 hours.</p>" +
                "<p>If you didn't request this, please ignore this email.</p>" +
                "</body>" +
                "</html>";
    }

    private void sendPasswordResetEmail(User user, String token) {
        try {
            String resetUrl = env.getProperty("app.base-url") + "/ui/auth/reset-password?token=" + token;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Password Reset Request");
            helper.setText(buildEmailContent(user.getUsername(), resetUrl), true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send reset email", e);
        }
    }


}