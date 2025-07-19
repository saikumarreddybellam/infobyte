package com.infobyte.task.project.services;

import com.infobyte.task.project.dtos.QuizAttemptDto;
import com.infobyte.task.project.dtos.QuizDto;
import com.infobyte.task.project.dtos.QuizResultDto;
import com.infobyte.task.project.dtos.QuizSubmissionDto;
import com.infobyte.task.project.dtos.QuizSummaryDto;

import java.util.List;

public interface QuizAttemptService {
    List<QuizSummaryDto> getAvailableQuizzes();
    QuizDto getQuizForUser(Long quizId);
    QuizResultDto evaluateQuiz(Long quizId, QuizSubmissionDto submission, String username);
    List<QuizAttemptDto> getUserAttempts(String username);
}
