package com.infobyte.task.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResultDto {
    private int score;
    private int totalQuestions;
    private List<Feedback> feedback;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Feedback {
        private String question;
        private String selectedAnswer;
        private String correctAnswer;
        private boolean isCorrect;
    }
}
