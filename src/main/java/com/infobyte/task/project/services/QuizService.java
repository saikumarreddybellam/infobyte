package com.infobyte.task.project.services;

import com.infobyte.task.project.dtos.QuizDto;

import java.util.List;

public interface QuizService {
    QuizDto createQuiz(QuizDto quizDto);
    QuizDto updateQuiz(Long id, QuizDto quizDto);
    void deleteQuiz(Long id);
    List<QuizDto> getAllQuizzes();

    QuizDto getQuizById(Long id);
}
