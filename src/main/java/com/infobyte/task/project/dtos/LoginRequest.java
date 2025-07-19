package com.infobyte.task.project.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
