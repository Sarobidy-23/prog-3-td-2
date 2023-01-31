package app.foot.controller.validator;

import app.foot.exception.BadRequestException;
import app.foot.controller.rest.Player;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.PlayerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class UpdatePlayerValidator implements Consumer<Player> {
    @Autowired
    private PlayerRepository repository;
    @Override
    public void accept(Player update) {
        StringBuilder exceptionMessage = new StringBuilder();
        PlayerEntity actualPlayer = repository.getById(update.getId());
        if(update.getName() == null){
            exceptionMessage.append("Name is mandatory for update player.");
        }
        if (update.getIsGuardian() == null){
            exceptionMessage.append("Guardian status is mandatory for update player.");
        }
        if (update.getTeamName() != actualPlayer.getTeam().getName()) {
            exceptionMessage.append("Only name and guardian status can modifyed in player.");
        }
        if (!exceptionMessage.isEmpty()){
            throw new BadRequestException(exceptionMessage.toString());
        }
    }
}
