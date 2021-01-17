package com.miladjafari.mancala.ws;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class GameResourceAddPlayerTest extends AbstractGameIT{

    @Test
    public void testSuccessAddPlayer() {
        String id = createNewGame();

        given()
                .when().post(String.format("/games/%s/addPlayer/%s", id, "Milad"))
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testSuccessAddTwoPlayers() {
        final String PLAYER_1 = "Milad";
        final String PLAYER_2 = "Elena";

        // create new game
        String id = createNewGame();

        //add player 1
        given()
                .when().post(String.format("/games/%s/addPlayer/%s", id, PLAYER_1))
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        //add player 2
        given()
                .when().post(String.format("/games/%s/addPlayer/%s", id, PLAYER_2))
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testFailAddPlayerWhenGameIdIsInvalid() {
        final String INVALID_GAME_ID = "INVALID_GAME_ID";

        given()
                .when().post(String.format("/games/%s/addPlayer/%s", INVALID_GAME_ID, "Milad"))
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    public void testFailAddPlayerWhenGamePlayerIsDuplicated() {
        final String DUPLICATED_PLAYER_NAME = "MILAD";

        String id = createNewGame();

        given()
                .when().post(String.format("/games/%s/addPlayer/%s", id, DUPLICATED_PLAYER_NAME))
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        given()
                .when().post(String.format("/games/%s/addPlayer/%s", id, DUPLICATED_PLAYER_NAME))
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .assertThat()
                .body("error", notNullValue());
    }

    @Test
    public void testFailAddPlayerWhenThereAreMoreThanThanTwoPlayer() {
        final String PLAYER_1 = "Milad";
        final String PLAYER_2 = "Elena";
        final String PLAYER_3 = "Asghar";

        // create new game
        String id = createNewGame();

        //add player 1
        given()
                .when().post(String.format("/games/%s/addPlayer/%s", id, PLAYER_1))
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        //add player 2
        given()
                .when().post(String.format("/games/%s/addPlayer/%s", id, PLAYER_2))
                .then()
                .statusCode(Response.Status.OK.getStatusCode());

        given()
                .when().post(String.format("/games/%s/addPlayer/%s", id, PLAYER_3))
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .assertThat()
                .body("error", notNullValue());
    }
}