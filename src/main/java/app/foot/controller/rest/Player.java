package app.foot.controller.rest;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Player {
    private Integer id;
    private String name;
    private String teamName;
    private Boolean isGuardian;
}
