package com.infobyte.task.project.services.impl;

import com.infobyte.task.project.dtos.QuizDto;
import com.infobyte.task.project.entities.Quiz;
import com.infobyte.task.project.entities.QuizAttempt;
import com.infobyte.task.project.repositories.QuestionRepository;
import com.infobyte.task.project.repositories.QuizAttemptRepository;
import com.infobyte.task.project.repositories.QuizRepository;
import com.infobyte.task.project.repositories.UserAnswerRepository;
import com.infobyte.task.project.services.QuizService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final UserAnswerRepository userAnswerRepository;
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
    @Transactional
    public void deleteQuiz(Long quizId) {
        // 1. Find all quiz attempts for this quiz
        List<QuizAttempt> attempts = quizAttemptRepository.findByQuizId(quizId);

        // 2. For each attempt, delete its user answers
        attempts.forEach(attempt -> {
            userAnswerRepository.deleteByQuizAttemptId(attempt.getId());
        });

        // 3. Delete all quiz attempts
        quizAttemptRepository.deleteByQuizId(quizId);

        // 4. Delete the quiz (which will cascade to questions and options)
        quizRepository.deleteById(quizId);
    }

    @Override
    public List<QuizDto> getAllQuizzes() {
        return quizRepository.findAll().stream()
                .map(quiz -> mapper.map(quiz, QuizDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public QuizDto getQuizById(Long id) {
        return quizRepository.findById(id)
                .map(quiz -> mapper.map(quiz, QuizDto.class))
                .orElseThrow(() -> new RuntimeException("Quiz not found with id: " + id));
    }

}
