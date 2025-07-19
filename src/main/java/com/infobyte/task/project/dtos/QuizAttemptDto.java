package com.infobyte.task.project.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizAttemptDto {
    private Long attemptId;
    private String quizTitle;
    private LocalDateTime attemptedOn;
    private int score;
    private int totalQuestions;
}
