package app.foot.controller;

import app.foot.model.CreatePlayerScore;
import app.foot.model.Match;
import app.foot.model.PlayerScorer;
import app.foot.repository.mapper.MatchMapper;
import app.foot.service.MatchService;
import app.foot.service.PlayerScoreService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MatchController {
    private final MatchService service;
    private final PlayerScoreService playerScoreService;
    private final MatchMapper mapper;

    @GetMapping("/matches")
    public List<Match> getMatches() {
        return service.getMatches();
    }
    @PostMapping("/matches/{matchId}/goals")
    public Match createGoals(@PathVariable int matchId, @RequestBody CreatePlayerScore toCreate) {
         playerScoreService.addScoreInMatch(matchId,toCreate);
         return mapper.toDomain(service.getMatchesById(matchId));
    }
}
