import app.foot.model.Match;
import app.foot.model.Team;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.TeamEntity;
import app.foot.repository.mapper.MatchMapper;
import app.foot.repository.mapper.PlayerMapper;
import app.foot.repository.mapper.TeamMapper;
import app.foot.utils.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FirstTest {
    TeamMapper teamMapper = mock(TeamMapper.class);
    PlayerMapper playerMapper = mock(PlayerMapper.class);
    MatchMapper subject = new MatchMapper(teamMapper,playerMapper);
    @Test
    public void to_domain_ok() {
        //Pour n'importe quel objet
        when(teamMapper.toDomain(any())).thenReturn(Team.builder().build());
        //Pour une valeur spécifique
        when(teamMapper.toDomain(eq(TeamEntity.builder()
                .id(1)
                .name("barea").build()))).thenReturn(Team.builder().build());
        Match actual = subject.toDomain(MatchEntity.builder()
                .scorers(List.of())
                .build());
        Match expected = Match.builder().build();
        assertEquals(expected,actual);
    }
}
