package com.infobyte.task.project.services.impl;

import com.infobyte.task.project.dtos.QuizDto;
import com.infobyte.task.project.entities.Quiz;
import com.infobyte.task.project.repositories.QuizRepository;
import com.infobyte.task.project.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final ModelMapper mapper;

    @Override
    public QuizDto createQuiz(QuizDto quizDto) {
        Quiz quiz = mapper.map(quizDto, Quiz.class);
        return mapper.map(quizRepository.save(quiz), QuizDto.class);
    }

    @Override
    public QuizDto updateQuiz(Long id, QuizDto quizDto) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        quiz.setTopic(quizDto.getTopic());
        quiz.setDifficulty(quizDto.getDifficulty());

        return mapper.map(quizRepository.save(quiz), QuizDto.class);
    }

    @Override
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }

    @Override
    public List<QuizDto> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(quiz -> mapper.map(quiz, QuizDto.class))
                .collect(Collectors.toList());
    }
}
