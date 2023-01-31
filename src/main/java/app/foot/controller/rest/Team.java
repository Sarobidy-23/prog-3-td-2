package app.foot.controller.rest;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
public class Team {
  private Integer id;
  private String name;
}
