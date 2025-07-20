package com.infobyte.task.project.controllers.ui;

import com.infobyte.task.project.dtos.QuizResultDto;
import com.infobyte.task.project.dtos.QuizSubmissionDto;
import com.infobyte.task.project.services.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/user/quizzes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserQuizUiController {

    private final QuizAttemptService quizAttemptService;

    @GetMapping
    public String showAvailableQuizzes(Model model) {
        model.addAttribute("quizzes", quizAttemptService.getAvailableQuizzes());
        return "user/quizzes/list";
    }

    @GetMapping("/{quizId}")
    public String showQuizForUser(@PathVariable Long quizId, Model model) {
        model.addAttribute("quiz", quizAttemptService.getQuizForUser(quizId));
        return "user/quizzes/attempt";
    }

    @PostMapping("/{quizId}/attempt")
    public String submitQuiz(
            @PathVariable Long quizId,
            @ModelAttribute QuizSubmissionDto submissionDto,
            Authentication authentication,
            Model model) {
        QuizResultDto result = quizAttemptService.evaluateQuiz(
                quizId,
                submissionDto,
                authentication.getName()
        );
        model.addAttribute("result", result);
        return "user/quizzes/result";
    }

    @GetMapping("/attempts")
    public String showQuizHistory(Authentication authentication, Model model) {
        model.addAttribute("attempts",
                quizAttemptService.getUserAttempts(authentication.getName()));
        return "user/quizzes/history";
    }
}