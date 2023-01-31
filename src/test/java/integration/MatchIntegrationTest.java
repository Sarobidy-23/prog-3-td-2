package integration;

import app.foot.FootApi;
import app.foot.controller.rest.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
@Slf4j
class MatchIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();  //Allow 'java.time.Instant' mapping

    @Test
    void read_match_by_id_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches/2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Match actual = objectMapper.readValue(
                response.getContentAsString(), Match.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedMatch2(), actual);
    }

    @Test
    void add_goals_into_match_id_3_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/matches/3/goals")
                        .content(objectMapper.writeValueAsString(List.of(playerScorer1())))
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Match actual = objectMapper.readValue(
                response.getContentAsString(), Match.class);
        Match expected = expectedMatch3();
        assertEquals(expected,actual);
    }

    @Test
    void add_goals_into_match_id_3_after_time_ko() throws Exception {
        String expectedException = "400 BAD_REQUEST : Player#J3 cannot score before after minute 90.";

        mockMvc.perform(post("/matches/3/goals")
                        .content(objectMapper.writeValueAsString(List.of(PlayerScorer.builder()
                                .player(player3())
                                .isOG(false)
                                .scoreTime(100)
                                .build())))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(expectedException, result.getResolvedException().getMessage()));
    }

    @Test
    void add_goals_into_match_id_3_before_time_ko() throws Exception {
        String expectedException = "400 BAD_REQUEST : Player#3 cannot score before before minute 0.";

        mockMvc.perform(post("/matches/3/goals")
                        .content(objectMapper.writeValueAsString(List.of(PlayerScorer.builder()
                                .player(player3())
                                .isOG(false)
                                .scoreTime(-10)
                                .build())))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(expectedException, result.getResolvedException().getMessage()));
    }

    @Test
    void add_goals_into_match_id_3_null_time_ko() throws Exception {
        String expectedException = "400 BAD_REQUEST : Score minute is mandatory.";

        mockMvc.perform(post("/matches/3/goals")
                        .content(objectMapper.writeValueAsString(List.of(PlayerScorer.builder()
                                .player(player3())
                                .isOG(false)
                                .scoreTime(null)
                                .build())))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(expectedException, result.getResolvedException().getMessage()));
    }

    @Test
    void add_goals_into_match_id_3_guardian_ko() throws Exception {
        String expectedException = "400 BAD_REQUEST : Player#7 is a guardian so they cannot score.";

        mockMvc.perform(post("/matches/3/goals")
                        .content(objectMapper.writeValueAsString(List.of(PlayerScorer.builder()
                                .player(playerGuardian())
                                .isOG(false)
                                .scoreTime(10)
                                .build())))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(expectedException, result.getResolvedException().getMessage()));
    }


    private static Match expectedMatch2() {
        return Match.builder()
                .id(2)
                .teamA(teamMatchA())
                .teamB(teamMatchB())
                .stadium("S2")
                .datetime(Instant.parse("2023-01-01T14:00:00Z"))
                .build();
    }

    private static Match expectedMatch3() {
        return Match.builder()
                .id(3)
                .teamA(teamMatchA1_3())
                .teamB(teamMatchB())
                .stadium("S3")
                .datetime(Instant.parse("2023-01-01T14:00:00Z")).build();
    }

    private static TeamMatch teamMatchA1_3() {
        return TeamMatch.builder()
                .team(team1())
                .score(1)
                .scorers(List.of(playerScorer1()))
                .build();
    }
    private static TeamMatch teamMatchB() {
        return TeamMatch.builder()
                .team(team3())
                .score(0)
                .scorers(List.of())
                .build();
    }

    private static TeamMatch teamMatchA() {
        return TeamMatch.builder()
                .team(team2())
                .score(2)
                .scorers(List.of(PlayerScorer.builder()
                                .player(player3())
                                .scoreTime(70)
                                .isOG(false)
                                .build(),
                        PlayerScorer.builder()
                                .player(player6())
                                .scoreTime(80)
                                .isOG(true)
                                .build()))
                .build();
    }

    private static Team team1() {
        return Team.builder()
                .id(1)
                .name("E1")
                .build();
    }
    private static Team team3() {
        return Team.builder()
                .id(3)
                .name("E3")
                .build();
    }

    private static Player player6() {
        return Player.builder()
                .id(6)
                .name("J6")
                .isGuardian(false)
                .build();
    }

    private static Player player3() {
        return Player.builder()
                .id(3)
                .name("J3")
                .teamName("E3")
                .isGuardian(false)
                .build();
    }

    private static Player player1() {
        return Player.builder()
                .id(1)
                .name("J1")
                .teamName("E1")
                .isGuardian(false)
                .build();
    }

    private static Player playerGuardian() {
        return Player.builder()
                .id(7)
                .name("G1")
                .teamName("E1")
                .isGuardian(true)
                .build();
    }

    private static Team team2() {
        return Team.builder()
                .id(2)
                .name("E2")
                .build();
    }

    private static PlayerScorer playerScorer1(){
        return PlayerScorer.builder()
                .player(player1())
                .scoreTime(20)
                .isOG(false)
                .build();
    }

}
