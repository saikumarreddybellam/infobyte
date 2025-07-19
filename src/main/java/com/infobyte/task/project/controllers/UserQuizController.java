package com.infobyte.task.project.controllers;

import com.infobyte.task.project.dtos.QuizAttemptDto;
import com.infobyte.task.project.dtos.QuizDto;
import com.infobyte.task.project.dtos.QuizResultDto;
import com.infobyte.task.project.dtos.QuizSubmissionDto;
import com.infobyte.task.project.dtos.QuizSummaryDto;
import com.infobyte.task.project.services.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/quiz")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserQuizController {

    private final QuizAttemptService quizAttemptService;

    @GetMapping("/available")
    public ResponseEntity<List<QuizSummaryDto>> getAvailableQuizzes() {
        return ResponseEntity.ok(quizAttemptService.getAvailableQuizzes());
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDto> getQuizDetails(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizAttemptService.getQuizForUser(quizId));
    }

    @PostMapping("/submit/{quizId}")
    public ResponseEntity<QuizResultDto> submitQuiz(
            @PathVariable Long quizId,
            @RequestBody QuizSubmissionDto submissionDto,
            Principal principal
    ) {
        return ResponseEntity.ok(quizAttemptService.evaluateQuiz(quizId, submissionDto, principal.getName()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<QuizAttemptDto>> getQuizHistory(Principal principal) {
        return ResponseEntity.ok(quizAttemptService.getUserAttempts(principal.getName()));
    }
}
