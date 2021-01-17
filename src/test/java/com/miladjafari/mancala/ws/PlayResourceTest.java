package com.miladjafari.mancala.ws;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class PlayResourceTest extends AbstractGameIT {

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


}