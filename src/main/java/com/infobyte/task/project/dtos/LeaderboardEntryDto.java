package com.infobyte.task.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardEntryDto {
    private String username;
    private int score;
    private LocalDateTime date;
}
