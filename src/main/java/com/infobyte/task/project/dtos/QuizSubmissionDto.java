package com.infobyte.task.project.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuizSubmissionDto {
    @NotNull(message = "Answers cannot be null")
    private List<AnswerSubmission> answers;

    @Data
    public static class AnswerSubmission {
        @NotNull(message = "Question ID is required")
        private Long questionId;

        @NotNull(message = "Selected option ID is required")
        private Long selectedOptionId;
    }
}
