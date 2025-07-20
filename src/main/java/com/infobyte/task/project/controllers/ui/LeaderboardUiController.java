package com.infobyte.task.project.controllers.ui;

import com.infobyte.task.project.services.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/leaderboard")
@RequiredArgsConstructor
public class LeaderboardUiController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/overall")
    public String showOverallLeaderboard(Model model) {
        model.addAttribute("entries", leaderboardService.getOverallTopScorers());
        return "leaderboard/overall";
    }

    @GetMapping("/quiz/{quizId}")
    public String showQuizLeaderboard(@PathVariable Long quizId, Model model) {
        model.addAttribute("entries", leaderboardService.getTopScorersForQuiz(quizId));
        model.addAttribute("quizId", quizId);
        return "leaderboard/quiz";
    }
}