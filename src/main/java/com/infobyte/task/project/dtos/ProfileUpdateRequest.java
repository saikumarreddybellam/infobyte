package com.infobyte.task.project.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String confirmPassword;

    private String email;
}