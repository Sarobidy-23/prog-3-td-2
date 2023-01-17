package app.foot.repository.mapper;

import app.foot.model.CreatePlayerScore;
import app.foot.model.PlayerScorer;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.service.MatchService;
import app.foot.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PlayerScoreMapper {
    private final PlayerMapper playerMapper;
    private final MatchService matchService;
    private final PlayerService playerService;
    public PlayerScorer toRest(PlayerScoreEntity domain) {
        return PlayerScorer.builder()
                .player(playerMapper.toDomain(domain.getPlayer()))
                .minute(domain.getMinute())
                .isOwnGoal(domain.isOwnGoal())
                .build();
    }
    public PlayerScoreEntity toDomain(int matchId, CreatePlayerScore playerScorer) {
        MatchEntity associatedMatch = matchService.getMatchesById(matchId);
        PlayerEntity associatedPlayer = playerService.getById(playerScorer.getPlayerId());
        return PlayerScoreEntity.builder()
                .match(associatedMatch)
                .player(associatedPlayer)
                .minute(playerScorer.getMinute())
                .ownGoal(playerScorer.isOwnGoal())
                .build();
    }
}
