package com.infobyte.task.project.controllers;

import com.infobyte.task.project.dtos.LeaderboardEntryDto;
import com.infobyte.task.project.services.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping("/{quizId}")
    public ResponseEntity<List<LeaderboardEntryDto>> getLeaderboardByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(leaderboardService.getTopScorersForQuiz(quizId));
    }

    @GetMapping("/overall")
    public ResponseEntity<List<LeaderboardEntryDto>> getOverallLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getOverallTopScorers());
    }
}
