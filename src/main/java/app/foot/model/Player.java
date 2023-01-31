package app.foot.model;

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
    private Boolean isGuardian;
    private String teamName;
}
