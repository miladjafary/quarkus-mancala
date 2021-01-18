package com.miladjafari.mancala.ws;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class PlayerResourceTest extends AbstractGameIT {

    private final String player1 = "milad";
    private final String player2 = "Elena";
    private String gameId;

    @BeforeEach
    public void beforeEach() {
        gameId = createNewGame();
        addPlayer(gameId, player1);
        addPlayer(gameId, player2);
    }

    private String createPlayUrl(String gameId, String player) {
        return String.format("/games/%s/%s/pit/%s", gameId, player, "1");
    }

    @Test
    public void testSuccessPlay() {
        given()
                .when().put(createPlayUrl(gameId, player1))
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .assertThat()
                .body("id", equalTo(gameId))
                .body("uri", endsWith(gameId))
                .body("status.1", equalTo(0))
                .body("status.2", equalTo(7))
                .body("status.3", equalTo(7))
                .body("status.4", equalTo(7))
                .body("status.5", equalTo(7))
                .body("status.6", equalTo(7))
                .body("status.7", equalTo(1))
                .body("status.8", equalTo(6))
                .body("status.9", equalTo(6))
                .body("status.10", equalTo(6))
                .body("status.11", equalTo(6))
                .body("status.12", equalTo(6))
                .body("status.13", equalTo(6))
                .body("status.14", equalTo(0))
        ;
    }

    @Test
    public void testSuccessFailPlayWhenGameIdNotFound() {
        final String INVALID_GAME_ID = "invalidGameId";
        given()
                .when().put(createPlayUrl(INVALID_GAME_ID, player1))
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .assertThat()
                .body("error", notNullValue())
        ;
    }

    @Test
    public void testSuccessFailPlayWhenPlayerNotFound() {
        final String INVALID_PLAYER_ID = "invalidPlayerId";
        given()
                .when().put(createPlayUrl(gameId, INVALID_PLAYER_ID))
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .assertThat()
                .body("error", notNullValue())
        ;
    }


}