package com.infobyte.task.project.services;

import com.infobyte.task.project.dtos.LoginRequest;
import com.infobyte.task.project.dtos.LoginResponse;
import com.infobyte.task.project.dtos.PasswordResetRequest;
import com.infobyte.task.project.dtos.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    void initiatePasswordReset(String email);
    void resetPassword(PasswordResetRequest request);

    void sendPasswordResetEmail(String email);
}
