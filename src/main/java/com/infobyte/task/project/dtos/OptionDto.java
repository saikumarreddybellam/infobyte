package com.infobyte.task.project.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OptionDto {
    private Long id;

    @NotBlank(message = "Option text is required")
    private String text;

    private boolean correct;  // Changed from isCorrect to correct

    private Long questionId;

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}