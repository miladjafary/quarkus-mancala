package com.miladjafari.mancala.ws;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class GameResourceGetInfoTest extends AbstractGameIT {

    private final String player1 = "milad";
    private final String player2 = "Elena";
    private String gameId;

    @BeforeEach
    public void beforeEach() {
        gameId = createNewGame();
        addPlayer(gameId, player1);
        addPlayer(gameId, player2);
    }

    private String createGameInfoUrl(String gameId, String player) {
        return String.format("/games/%s/%s", gameId, player);
    }

    @Test
    public void testSuccessPlay() {
        given()
                .when().get(createGameInfoUrl(gameId, player1))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .assertThat()
                .body("gameId", equalTo(gameId))
                .body("player", equalTo(player1))
                .body("opponent", equalTo(player2))
                .body("gameOver", is(false))
                .body("nextTurn", equalTo(player1))
                .body("winner", equalTo(""))
                .body("boardStatus.1", equalTo(6))
                .body("boardStatus.2", equalTo(6))
                .body("boardStatus.3", equalTo(6))
                .body("boardStatus.4", equalTo(6))
                .body("boardStatus.5", equalTo(6))
                .body("boardStatus.6", equalTo(6))
                .body("boardStatus.7", equalTo(0))
                .body("boardStatus.8", equalTo(6))
                .body("boardStatus.9", equalTo(6))
                .body("boardStatus.10", equalTo(6))
                .body("boardStatus.11", equalTo(6))
                .body("boardStatus.12", equalTo(6))
                .body("boardStatus.13", equalTo(6))
                .body("boardStatus.14", equalTo(0))
        ;
    }

    @Test
    public void testFailPlayWhenGameIdNotFound() {
        final String INVALID_GAME_ID = "invalidGameId";
        given()
                .when().get(createGameInfoUrl(INVALID_GAME_ID, player1))
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .assertThat()
                .body("error", notNullValue())
        ;
    }

    @Test
    public void testFailPlayWhenPlayerNotFound() {
        final String INVALID_PLAYER_ID = "invalidPlayerId";
        given()
                .when().get(createGameInfoUrl(gameId, INVALID_PLAYER_ID))
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .assertThat()
                .body("error", notNullValue())
        ;
    }
}