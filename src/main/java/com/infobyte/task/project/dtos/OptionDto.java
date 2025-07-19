package com.infobyte.task.project.dtos;

import lombok.Data;

@Data
public class OptionDto {
    private Long id;
    private String text;
    private boolean isCorrect;
}
