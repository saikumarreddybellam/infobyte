package com.infobyte.task.project.dtos;

import lombok.Data;

import java.util.List;

@Data
public class QuizDto {
    private Long id;
    private String title;
    private String description;
    private String topic;
    private String difficulty; // Optional: "EASY", "MEDIUM", "HARD"
    private List<QuestionDto> questions;
}
