package com.infobyte.task.project.controllers.ui;

import com.infobyte.task.project.dtos.QuestionDto;
import com.infobyte.task.project.dtos.QuizDto;
import com.infobyte.task.project.services.QuestionService;
import com.infobyte.task.project.services.QuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ui/admin/quizzes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Slf4j
public class QuizUiController {

    private final QuizService quizService;
    private final QuestionService questionService;

    @GetMapping
    public String showAllQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "admin/quizzes/list";
    }

    @GetMapping("/create")
    public String showCreateQuizForm(Model model, Authentication authentication) {
        model.addAttribute("quizDto", new QuizDto());
        return "admin/quizzes/create";
    }

    @PostMapping
    public String createQuiz(@ModelAttribute QuizDto quizDto) {
        quizService.createQuiz(quizDto);
        return "redirect:/ui/admin/quizzes";
    }

    @GetMapping("/{id}/edit")
    public String showEditQuizForm(@PathVariable Long id, Model model) {
        model.addAttribute("quizDto", quizService.getQuizById(id));
        return "admin/quizzes/edit";
    }

    @PostMapping("/{id}")
    public String updateQuiz(@PathVariable Long id, @ModelAttribute QuizDto quizDto) {
        quizService.updateQuiz(id, quizDto);
        return "redirect:/ui/admin/quizzes";
    }

    @PostMapping("/{id}/delete")
    public String deleteQuiz(@PathVariable Long id) {
        quizService.deleteQuiz(id);
        return "redirect:/ui/admin/quizzes";
    }

    @GetMapping("/{quizId}/questions")
    public String showQuizQuestions(@PathVariable Long quizId, Model model) {
        QuizDto quiz = quizService.getQuizById(quizId);
        model.addAttribute("quiz", quiz);
        return "admin/questions/list";
    }

    @GetMapping("/{quizId}/questions/create")
    public String showCreateQuestionForm(@PathVariable Long quizId, Model model) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setQuizId(quizId);
        model.addAttribute("questionDto", questionDto);
        model.addAttribute("quizId", quizId);
        return "admin/questions/create";
    }

    @PostMapping("/{quizId}/questions")
    public String createQuestion(
            @PathVariable Long quizId,
            @ModelAttribute QuestionDto questionDto,
            @RequestParam("correctOption") int correctOptionIndex,
            BindingResult bindingResult,
            Model model) {

        if (correctOptionIndex < 0 || correctOptionIndex >= questionDto.getOptions().size()) {
            bindingResult.rejectValue("options", "invalid.correctOption", "Please select a valid correct option");
        }

        if (questionDto.getOptions() == null || questionDto.getOptions().size() < 2) {
            bindingResult.rejectValue("options", "size.min", "At least 2 options are required");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("quizId", quizId);
            return "admin/questions/create";
        }

        questionDto.getOptions().forEach(option -> option.setCorrect(false));
        questionDto.getOptions().get(correctOptionIndex).setCorrect(true);

        questionService.addQuestionToQuiz(quizId, questionDto);
        return "redirect:/ui/admin/quizzes/" + quizId + "/questions";
    }
}