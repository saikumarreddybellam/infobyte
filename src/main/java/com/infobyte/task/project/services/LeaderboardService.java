package com.infobyte.task.project.services;

import com.infobyte.task.project.dtos.LeaderboardEntryDto;

import java.util.List;

public interface LeaderboardService {
    List<LeaderboardEntryDto> getTopScorersForQuiz(Long quizId);
    List<LeaderboardEntryDto> getOverallTopScorers();
}
