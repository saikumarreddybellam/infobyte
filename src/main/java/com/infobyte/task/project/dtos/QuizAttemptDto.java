package com.infobyte.task.project.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizAttemptDto {
    private Long attemptId;
    private Long quizId;
    private String quizTitle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime attemptedOn;

    private int score;
    private int totalQuestions;
}