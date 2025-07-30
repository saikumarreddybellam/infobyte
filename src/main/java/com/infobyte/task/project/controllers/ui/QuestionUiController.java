package com.infobyte.task.project.controllers.ui;

import com.infobyte.task.project.dtos.OptionDto;
import com.infobyte.task.project.dtos.QuestionDto;
import com.infobyte.task.project.services.QuestionService;
import com.infobyte.task.project.services.QuizService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/ui/admin/questions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class QuestionUiController {

    private final QuestionService questionService;
    private final QuizService quizService;

    @GetMapping("/{quizId}/create")
    public String showCreateQuestionForm(@PathVariable Long quizId, Model model) {
        model.addAttribute("quizId", quizId);
        model.addAttribute("questionDto", new QuestionDto());
        return "admin/questions/create";
    }

    @PostMapping("/{quizId}")
    public String createQuestion(
            @PathVariable Long quizId,
            @ModelAttribute QuestionDto questionDto,
            Model model) {
        try {
            QuestionDto createdQuestion = questionService.addQuestionToQuiz(quizId, questionDto);
            return "redirect:/ui/admin/quizzes/" + quizId + "/questions";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create question: " + e.getMessage());
            model.addAttribute("quizId", quizId);
            model.addAttribute("questionDto", questionDto);
            return "admin/questions/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditQuestionForm(@PathVariable Long id, Model model) {
        QuestionDto questionDto = questionService.getQuestionById(id);

        // Ensure options list is never null
        if (questionDto.getOptions() == null) {
            questionDto.setOptions(new ArrayList<>());
        }

        model.addAttribute("questionDto", questionDto);
        model.addAttribute("quizId", questionDto.getQuizId());
        return "admin/questions/edit";
    }

    @PostMapping("/{id}/update")
    public String updateQuestion(
            @PathVariable Long id,
            @ModelAttribute @Valid QuestionDto questionDto,
            BindingResult bindingResult,
            Model model) {
        try {
            // Ensure options list is never null
            if (questionDto.getOptions() == null) {
                questionDto.setOptions(new ArrayList<>());
                bindingResult.rejectValue("options", "NotEmpty", "At least 2 options are required");
            }

            // Validate at least one option is marked as correct
            boolean hasCorrectOption = questionDto.getOptions().stream()
                    .anyMatch(OptionDto::isCorrect);

            if (!hasCorrectOption) {
                bindingResult.rejectValue("options", "CorrectOptionRequired", "At least one option must be marked as correct");
            }

            if (bindingResult.hasErrors()) {
                model.addAttribute("questionDto", questionDto);
                model.addAttribute("quizId", questionDto.getQuizId());
                return "admin/questions/edit";
            }

            QuestionDto updatedQuestion = questionService.updateQuestion(id, questionDto);
            return "redirect:/ui/admin/quizzes/" + questionDto.getQuizId() + "/questions";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update question: " + e.getMessage());
            model.addAttribute("questionDto", questionDto);
            model.addAttribute("quizId", questionDto.getQuizId());
            return "admin/questions/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteQuestion(@PathVariable Long id) {
        QuestionDto question = questionService.getQuestionById(id);
        questionService.deleteQuestion(id);
        return "redirect:/ui/admin/quizzes/" + question.getQuizId() + "/questions";
    }

    @GetMapping("/{quizId}/list")
    public String listQuestionsByQuiz(@PathVariable Long quizId, Model model) {
        model.addAttribute("quiz", quizService.getQuizById(quizId));
        return "admin/questions/list";
    }
}