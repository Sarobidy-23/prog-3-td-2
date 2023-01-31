package app.foot.controller.rest;

import java.time.Instant;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Match {
  private Integer id;
  private TeamMatch teamA;
  private TeamMatch teamB;
  private String stadium;
  private Instant datetime;
}
