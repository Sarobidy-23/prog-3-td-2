package app.foot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlayerScore {
    private Integer playerId;
    private Integer minute;
    private boolean isOwnGoal;
}
