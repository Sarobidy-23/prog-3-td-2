package app.foot.controller.validator;

import app.foot.controller.rest.Player;
import app.foot.exception.BadRequestException;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.PlayerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class PlayerValidator implements Consumer<Player> {
    @Autowired
    private PlayerRepository repository;
    @Override
    @Async
    public void accept(Player player) {
        StringBuilder exceptionMessage = new StringBuilder();
        PlayerEntity actualPlayer = repository.getById(player.getId());
        if(player.getName() == null){
            exceptionMessage.append("Name is mandatory for update player.");
        }
        if (player.getIsGuardian() == null){
            exceptionMessage.append("Guardian status is mandatory for update player.");
        }
        if (!player.getTeamName().equals(actualPlayer.getTeam().getName())) {
            exceptionMessage.append("Only name and guardian status can modifyed in player.");
        }
        if (!exceptionMessage.isEmpty()){
            throw new BadRequestException(exceptionMessage.toString());
        }
    }
}
