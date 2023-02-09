package integration;

import app.foot.FootApi;
import app.foot.controller.rest.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void read_matches_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        List<Match> actual = convertFromHttpResponse(response);

        assertEquals(3, actual.size());
        assertTrue(actual.contains(expectedMatch2()));
        assertTrue(actual.contains(expectedMatch1()));
        assertTrue(actual.contains(expectedMatch3()));
    }

    @Test
    void add_goals_into_match_id3_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/matches/3/goals")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(objectMapper.writeValueAsString(
                                List.of(PlayerScorer.builder()
                                        .player(player6())
                                        .isOG(false)
                                        .scoreTime(5)
                                        .build())
                        )))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Match expected = expectedMatch3();
        Match actual = objectMapper.readValue(
                response.getContentAsString(), Match.class);
        assertEquals(expected,actual);
    }

    @Test
    void add_goals_into_match_id3_with_guardian_timescore_ko() throws Exception {
        String expectedException = "400 BAD_REQUEST : Player#7 is a guardian so they cannot score.";

        mockMvc.perform(post("/matches/3/goals")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(objectMapper.writeValueAsString(
                                List.of(PlayerScorer.builder()
                                        .player(playerGuardin())
                                        .isOG(false)
                                        .scoreTime(2)
                                        .build())
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(expectedException, result.getResolvedException().getMessage()));
    }
    @Test
    void add_goals_into_match_id3_with_null_timescore_ko() throws Exception {
        String expectedException = "400 BAD_REQUEST : Score minute is mandatory.";

        mockMvc.perform(post("/matches/3/goals")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(objectMapper.writeValueAsString(
                                List.of(PlayerScorer.builder()
                                        .player(player6())
                                        .isOG(false)
                                        .scoreTime(null)
                                        .build())
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(expectedException, result.getResolvedException().getMessage()));
    }

    @Test
    void add_goals_into_match_id3_with_after_timescore_ko() throws Exception {
        String expectedException = "400 BAD_REQUEST : Player#6 cannot score before after minute 90.";

        mockMvc.perform(post("/matches/3/goals")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(objectMapper.writeValueAsString(
                                List.of(PlayerScorer.builder()
                                        .player(player6())
                                        .isOG(false)
                                        .scoreTime(100)
                                        .build())
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(expectedException, result.getResolvedException().getMessage()));
    }

    @Test
    void add_goals_into_match_id3_with_before_timescore_ko() throws Exception {
        String expectedException = "400 BAD_REQUEST : Player#6 cannot score before before minute 0.";

        mockMvc.perform(post("/matches/3/goals")
                        .contentType("application/json")
                        .accept("application/json")
                        .content(objectMapper.writeValueAsString(
                                List.of(PlayerScorer.builder()
                                        .player(player6())
                                        .isOG(false)
                                        .scoreTime(-10)
                                        .build())
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(expectedException, result.getResolvedException().getMessage()));
    }
    private static Match expectedMatch1() {
        return Match.builder()
                .id(1)
                .teamA(teamMatchA1())
                .teamB(teamMatchB1())
                .stadium("S1")
                .datetime(Instant.parse("2023-01-01T10:00:00Z"))
                .build();
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
                .teamA(teamMatchA3())
                .teamB(teamMatchB3())
                .stadium("S3")
                .datetime(Instant.parse("2023-01-01T18:00:00Z"))
                .build();
    }

    private static TeamMatch teamMatchB() {
        return TeamMatch.builder()
                .team(team3())
                .score(0)
                .scorers(List.of())
                .build();
    }

    private static TeamMatch teamMatchA3() {
        return TeamMatch.builder()
                .team(team1())
                .score(0)
                .scorers(List.of())
                .build();
    }
    private static TeamMatch teamMatchA1() {
        return TeamMatch.builder()
                .team(team1())
                .score(4)
                .scorers(List.of(
                        PlayerScorer.builder()
                                .player(player1())
                                .scoreTime(30)
                                .isOG(false)
                                .build(),
                        PlayerScorer.builder()
                                .player(player1())
                                .scoreTime(20)
                                .isOG(false)
                                .build(),
                        PlayerScorer.builder()
                                .player(player1())
                                .scoreTime(10)
                                .isOG(false)
                                .build(),
                        PlayerScorer.builder()
                                .player(player4())
                                .scoreTime(60)
                                .isOG(true)
                                .build()
                ))
                .build();
    }

    private static TeamMatch teamMatchB1() {
        return TeamMatch.builder()
                .team(team2())
                .score(2)
                .scorers(List.of(
                        PlayerScorer.builder()
                                .player(player2())
                                .scoreTime(40)
                                .isOG(true)
                                .build(),
                        PlayerScorer.builder()
                                .player(player3())
                                .scoreTime(50)
                                .isOG(false)
                                .build()
                ))
                .build();
    }



    private static TeamMatch teamMatchB3() {
        return TeamMatch.builder()
                .team(team3())
                .score(1)
                .scorers(List.of(
                        PlayerScorer.builder()
                                .player(player6())
                                .scoreTime(5)
                                .isOG(false)
                                .build()
                ))
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


    private static Team team3() {
        return Team.builder()
                .id(3)
                .name("E3")
                .build();
    }
    private static Team team1() {
        return Team.builder()
                .id(1)
                .name("E1")
                .build();
    }

    private static Player player6() {
        return Player.builder()
                .id(6)
                .name("J6")
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

    private static Player player2() {
        return Player.builder()
                .id(2)
                .name("J2")
                .teamName("E1")
                .isGuardian(false)
                .build();
    }
    private static Player player4() {
        return Player.builder()
                .id(4)
                .name("J4")
                .teamName("E2")
                .isGuardian(false)
                .build();
    }

    private static Player player3() {
        return Player.builder()
                .id(3)
                .name("J3")
                .teamName("E2")
                .isGuardian(false)
                .build();
    }
    private static Player playerGuardin() {
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

    private List<Match> convertFromHttpResponse(MockHttpServletResponse response)
            throws JsonProcessingException, UnsupportedEncodingException {
        CollectionType playerListType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Match.class);
        return objectMapper.readValue(
                response.getContentAsString(),
                playerListType);
    }
}
