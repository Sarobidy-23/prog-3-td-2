package app.foot.controller.rest;

import app.foot.repository.entity.TeamEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateMatch {
    private TeamEntity teamA;
    private TeamEntity teamB;
    private String stadium;
    private Instant datetime;
}
