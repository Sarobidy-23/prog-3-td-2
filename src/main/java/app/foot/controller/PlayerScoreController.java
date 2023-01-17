package app.foot.controller;

import app.foot.model.CreatePlayerScore;
import app.foot.model.PlayerScorer;
import app.foot.service.PlayerScoreService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PlayerScoreController {
    private final PlayerScoreService service;
    @PostMapping("/matches/{matchId}/goals")
    public PlayerScorer createGoals(@PathVariable int matchId, @RequestBody CreatePlayerScore toCreate) {
        return service.addScoreInMatch(matchId,toCreate);
    }
}
