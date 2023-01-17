package app.foot.service;

import app.foot.model.CreatePlayerScore;
import app.foot.model.PlayerScorer;
import app.foot.model.exception.NotAuthorizedException;
import app.foot.repository.PlayerScoreRepository;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.mapper.PlayerScoreMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlayerScoreService {
    private final PlayerScoreRepository repository;
    private final PlayerScoreMapper mapper;

    public PlayerScorer addScoreInMatch(int matchId, CreatePlayerScore playerScorer) {
       PlayerScoreEntity toCreate = mapper.toDomain(matchId,playerScorer);
        if (toCreate.getPlayer().isGuardian() == false && toCreate.getMinute()>=0 && toCreate.getMinute() <= 90) {
            return mapper.toRest(repository.save(toCreate));
        } else if (toCreate.getPlayer().isGuardian()){
            throw new NotAuthorizedException("Player.Id." + playerScorer.getPlayerId() + ".is.Guardian.and.cannot.goals");
        }else {
            throw new NotAuthorizedException("Minute.of.goal."+toCreate.getMinute()+".is.outside.the.time.of.the.match");
        }
    }
}
