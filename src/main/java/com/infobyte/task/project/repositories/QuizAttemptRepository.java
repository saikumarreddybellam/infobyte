package com.infobyte.task.project.repositories;

import com.infobyte.task.project.entities.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByUserId(Long userId);
    List<QuizAttempt> findByUserUsername(String username);
    List<QuizAttempt> findByQuizIdOrderByScoreDesc(Long quizId);

    long countByUserUsername(String username);

    @Query("SELECT AVG(a.score) FROM QuizAttempt a WHERE a.user.username = :username")
    Optional<Double> getAverageScoreByUser(@Param("username") String username);

}
