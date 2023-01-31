package app.foot.model;

import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@ToString
@EqualsAndHashCode
public class Team {
    private Integer id;
    private String name;
}
