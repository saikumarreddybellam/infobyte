package com.infobyte.task.project.services;

import com.infobyte.task.project.dtos.QuestionDto;

public interface QuestionService {
    QuestionDto addQuestionToQuiz(Long quizId, QuestionDto questionDto);
    QuestionDto updateQuestion(Long id, QuestionDto questionDto);
    void deleteQuestion(Long id);
}
