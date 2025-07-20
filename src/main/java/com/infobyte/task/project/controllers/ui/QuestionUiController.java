package com.infobyte.task.project.controllers.ui;

import com.infobyte.task.project.dtos.QuestionDto;
import com.infobyte.task.project.services.QuestionService;
import com.infobyte.task.project.services.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("questionDto", questionDto);
        model.addAttribute("quizId", questionDto.getQuizId());
        return "admin/questions/edit";
    }

    @PostMapping("/{id}/update")
    public String updateQuestion(
            @PathVariable Long id,
            @ModelAttribute QuestionDto questionDto,
            Model model) {
        try {
            QuestionDto updatedQuestion = questionService.updateQuestion(id, questionDto);
            return "redirect:/ui/admin/quizzes/" + questionDto.getQuizId() + "/questions";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update question: " + e.getMessage());
            model.addAttribute("questionDto", questionDto);
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