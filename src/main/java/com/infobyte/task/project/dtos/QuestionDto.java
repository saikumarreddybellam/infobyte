package com.infobyte.task.project.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private Long id;

    @NotBlank(message = "Question text is required")
    @Size(max = 500, message = "Question text cannot exceed 500 characters")
    private String questionText;

    @NotNull(message = "Options list cannot be null")
    @Size(min = 2, message = "At least 2 options are required")
    private List<OptionDto> options;

    private Long quizId;
}
