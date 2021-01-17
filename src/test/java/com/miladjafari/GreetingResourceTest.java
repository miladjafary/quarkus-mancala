package com.miladjafari;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class GreetingResourceTest {

    @Test
    public void testCreateNewGame() {
        given()
                .when().post("/games")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .assertThat()
                .body("id", notNullValue())
                .body("uri", notNullValue());
    }

    @Test
    public void testSuccessAddPlayer() {
        String id = given()
                .when().post("/games")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract()
                .path("id");

        given()
                .when().post(String.format("/games/%s/addPlayer/%s", id, "Milad"))
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

        String id = given()
                .when().post("/games")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract()
                .path("id");

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
}