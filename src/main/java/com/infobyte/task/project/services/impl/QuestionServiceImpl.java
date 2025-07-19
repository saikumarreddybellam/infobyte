package com.infobyte.task.project.services.impl;

import com.infobyte.task.project.dtos.QuestionDto;
import com.infobyte.task.project.entities.Option;
import com.infobyte.task.project.entities.Question;
import com.infobyte.task.project.entities.Quiz;
import com.infobyte.task.project.repositories.QuestionRepository;
import com.infobyte.task.project.repositories.QuizRepository;
import com.infobyte.task.project.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final ModelMapper mapper;

    @Override
    public QuestionDto addQuestionToQuiz(Long quizId, QuestionDto questionDto) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        Question question = mapper.map(questionDto, Question.class);
        question.setQuiz(quiz);

        return mapper.map(questionRepository.save(question), QuestionDto.class);
    }

    @Override
    public QuestionDto updateQuestion(Long id, QuestionDto questionDto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        question.setQuestionText(questionDto.getQuestionText());
        question.getOptions().clear();
        question.getOptions().addAll(
                questionDto.getOptions().stream()
                        .map(optDto -> {
                            Option opt = new Option();
                            opt.setText(optDto.getText());
                            opt.setCorrect(optDto.isCorrect());
                            opt.setQuestion(question);
                            return opt;
                        }).collect(Collectors.toList())
        );

        return mapper.map(questionRepository.save(question), QuestionDto.class);
    }

    @Override
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
