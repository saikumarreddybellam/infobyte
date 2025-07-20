package com.infobyte.task.project.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OptionDto {
    private Long id;

    @NotBlank(message = "Option text is required")
    private String text;

    private boolean isCorrect;

    private Long questionId; // Added for better handling in forms
}
