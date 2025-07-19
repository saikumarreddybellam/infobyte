package com.infobyte.task.project.dtos;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private Long id;
    private String questionText;
    private List<OptionDto> options;
}
