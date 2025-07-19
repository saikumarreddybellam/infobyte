package com.infobyte.task.project.dtos;

import lombok.Data;

import java.util.List;

@Data
public class QuizSubmissionDto {
    private List<AnswerSubmission> answers;

    @Data
    public static class AnswerSubmission {
        private Long questionId;
        private Long selectedOptionId;
    }
}
