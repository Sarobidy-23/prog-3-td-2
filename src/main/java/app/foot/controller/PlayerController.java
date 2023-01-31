package app.foot.controller;

import app.foot.controller.rest.Player;
import app.foot.controller.rest.mapper.PlayerRestMapper;
import app.foot.controller.validator.GoalValidator;
import app.foot.controller.validator.UpdatePlayerValidator;
import app.foot.repository.TeamRepository;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.service.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class PlayerController {
    private final PlayerRestMapper mapper;
    private final PlayerService service;
    private final UpdatePlayerValidator validator;


    @GetMapping("/players")
    public List<Player> getPlayers() {
        return service.getPlayers().stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());
    }

    @PostMapping("/players")
    public List<Player> addPlayers(@RequestBody List<Player> toCreate) {
        List<app.foot.model.Player> domain = toCreate.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
        return service.createPlayers(domain).stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());
    }

    @PutMapping("/players")
    public List<Player> updateNameOrStatusPlayer(@RequestBody List<Player> toUpdate){
        toUpdate.forEach(validator);
        List<app.foot.model.Player> domain = toUpdate.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
        return service.updatePlayers(domain).stream()
                .map(mapper::toRest)
                .collect(Collectors.toUnmodifiableList());

    }
}
