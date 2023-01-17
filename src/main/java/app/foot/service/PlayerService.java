package app.foot.service;

import app.foot.model.exception.NotFoundException;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.PlayerEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository repository;
    public PlayerEntity getById(int playerId){
        Optional<PlayerEntity> player = repository.findById(playerId);
        if(player.isPresent()) {
            return player.get();
        }else {
            throw new NotFoundException("Player.id."+playerId+".Not.Found");
        }
    }
}
