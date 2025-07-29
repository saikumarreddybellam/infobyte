package com.infobyte.task.project.services.impl;

import com.infobyte.task.project.dtos.OptionDto;
import com.infobyte.task.project.dtos.QuestionDto;
import com.infobyte.task.project.dtos.QuizAttemptDto;
import com.infobyte.task.project.dtos.QuizDto;
import com.infobyte.task.project.dtos.QuizResultDto;
import com.infobyte.task.project.dtos.QuizSubmissionDto;
import com.infobyte.task.project.dtos.QuizSummaryDto;
import com.infobyte.task.project.entities.Option;
import com.infobyte.task.project.entities.Question;
import com.infobyte.task.project.entities.Quiz;
import com.infobyte.task.project.entities.QuizAttempt;
import com.infobyte.task.project.entities.User;
import com.infobyte.task.project.entities.UserAnswer;
import com.infobyte.task.project.repositories.OptionRepository;
import com.infobyte.task.project.repositories.QuestionRepository;
import com.infobyte.task.project.repositories.QuizAttemptRepository;
import com.infobyte.task.project.repositories.QuizRepository;
import com.infobyte.task.project.repositories.UserRepository;
import com.infobyte.task.project.services.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizAttemptServiceImpl implements QuizAttemptService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    @Override
    public List<QuizSummaryDto> getAvailableQuizzes() {
        return quizRepository.findAll().stream()
                .map(q -> new QuizSummaryDto(
                        q.getId(),
                        q.getTitle(),
                        q.getTopic(),
                        q.getDescription(),
                        q.getDifficulty(),
                        q.getQuestions().size()))
                .collect(Collectors.toList());
    }

    @Override
    public QuizDto getQuizForUser(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        QuizDto dto = new QuizDto();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setTopic(quiz.getTopic());
        dto.setDifficulty(quiz.getDifficulty());
        dto.setQuestions(
                quiz.getQuestions().stream().map(q -> {
                    QuestionDto qdto = new QuestionDto();
                    qdto.setId(q.getId());
                    qdto.setQuestionText(q.getQuestionText());
                    qdto.setOptions(
                            q.getOptions().stream().map(o -> {
                                OptionDto od = new OptionDto();
                                od.setId(o.getId());
                                od.setText(o.getText());
                                od.setCorrect(false); // hide correct info
                                return od;
                            }).collect(Collectors.toList()));
                    return qdto;
                }).collect(Collectors.toList())
        );
        return dto;
    }

    @Override
    public QuizResultDto evaluateQuiz(Long quizId, QuizSubmissionDto submission, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        int score = 0;
        List<QuizResultDto.Feedback> feedbackList = new ArrayList<>();

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setAttemptTime(LocalDateTime.now());

        List<UserAnswer> userAnswers = new ArrayList<>();

        for (QuizSubmissionDto.AnswerSubmission ans : submission.getAnswers()) {
            Question question = questionRepository.findById(ans.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            Option selected = optionRepository.findById(ans.getSelectedOptionId())
                    .orElseThrow(() -> new RuntimeException("Option not found"));

            boolean correct = selected.isCorrect();
            if (correct) score++;

            UserAnswer ua = new UserAnswer();
            ua.setQuizAttempt(attempt);
            ua.setQuestion(question);
            ua.setSelectedOption(selected);
            ua.setCorrect(correct);

            userAnswers.add(ua);

            feedbackList.add(new QuizResultDto.Feedback(
                    question.getQuestionText(),
                    selected.getText(),
                    question.getOptions().stream().filter(Option::isCorrect).findFirst().map(Option::getText).orElse(""),
                    correct
            ));
        }

        attempt.setScore(score);
        attempt.setUserAnswers(userAnswers);
        quizAttemptRepository.save(attempt);

        return new QuizResultDto(score, submission.getAnswers().size(), feedbackList);
    }

    @Override
    public List<QuizAttemptDto> getUserAttempts(String username) {
        return quizAttemptRepository.findByUserUsername(username).stream()
                .map(attempt -> {
                    QuizAttemptDto dto = new QuizAttemptDto();
                    dto.setAttemptId(attempt.getId());
                    dto.setQuizTitle(attempt.getQuiz().getTitle());
                    dto.setAttemptedOn(attempt.getAttemptTime());
                    dto.setScore(attempt.getScore());
                    dto.setTotalQuestions(attempt.getUserAnswers().size());
                    return dto;
                }).collect(Collectors.toList());
    }
}
