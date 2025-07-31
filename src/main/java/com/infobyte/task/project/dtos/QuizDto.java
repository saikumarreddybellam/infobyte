package com.infobyte.task.project.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class QuizDto {
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Topic is required")
    private String topic;

    private String difficulty;

    @NotNull(message = "Questions list cannot be null")
    private List<QuestionDto> questions = new ArrayList<>();
}