package com.infobyte.task.project.repositories;

import com.infobyte.task.project.entities.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUserId(Long userId);
    List<QuizAttempt> findByUserUsername(String username);
    List<QuizAttempt> findByQuizIdOrderByScoreDesc(Long quizId);
}
