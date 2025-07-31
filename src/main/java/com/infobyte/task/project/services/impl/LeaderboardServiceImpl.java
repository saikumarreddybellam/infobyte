package com.infobyte.task.project.services.impl;

import com.infobyte.task.project.dtos.LeaderboardEntryDto;
import com.infobyte.task.project.entities.QuizAttempt;
import com.infobyte.task.project.repositories.QuizAttemptRepository;
import com.infobyte.task.project.repositories.UserRepository;
import com.infobyte.task.project.services.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {

    private final QuizAttemptRepository quizAttemptRepository;
    private final UserRepository userRepository;

    @Override
    public List<LeaderboardEntryDto> getTopScorersForQuiz(Long quizId) {
        return quizAttemptRepository.findByQuizIdOrderByScoreDesc(quizId).stream()
                .limit(10)
                .map(attempt -> new LeaderboardEntryDto(
                        attempt.getUser().getUsername(),
                        attempt.getScore(),
                        attempt.getAttemptTime()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaderboardEntryDto> getOverallTopScorers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    List<QuizAttempt> attempts = quizAttemptRepository.findByUserId(user.getId());
                    int totalScore = attempts.stream().mapToInt(QuizAttempt::getScore).sum();
                    return new LeaderboardEntryDto(user.getUsername(), totalScore, null);
                })
                .sorted((a, b) -> Integer.compare(b.getScore(), a.getScore()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
