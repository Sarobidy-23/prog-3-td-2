package app.foot.controller.rest.mapper;

import app.foot.controller.rest.CreateMatch;
import app.foot.controller.rest.Match;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.TeamEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class MatchRestMapper {
  private final TeamMatchMapper teamMatchMapper;

  public Match toRest(app.foot.model.Match match) {
    return Match.builder()
        .id(match.getId())
        .datetime(match.getDatetime())
        .teamA(teamMatchMapper.toRest(match.getTeamA()))
        .teamB(teamMatchMapper.toRest(match.getTeamB()))
        .stadium(match.getStadium())
        .build();
  }

  public MatchEntity toEntity(CreateMatch domain) {
      return MatchEntity.builder()
              .teamA(TeamEntity.builder()
                      .id(domain.getTeamA().getId())
                      .name(domain.getTeamA().getName()).build())
              .teamB(TeamEntity.builder()
                      .id(domain.getTeamB().getId())
                      .name(domain.getTeamB().getName()).build())
              .scorers(List.of())
              .stadium(domain.getStadium())
              .datetime(domain.getDatetime())
              .build();
  }
}
