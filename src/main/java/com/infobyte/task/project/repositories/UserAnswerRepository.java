package com.infobyte.task.project.repositories;

import com.infobyte.task.project.entities.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByQuizAttemptId(Long attemptId);

    void deleteByQuizAttemptId(Long id);
}
