package com.infobyte.task.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizSummaryDto {
    private Long id;
    private String title;
    private String topic;
    private String difficulty;
    private int totalQuestions;
}
