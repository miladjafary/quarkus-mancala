package com.miladjafari.mancala.ws;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class GameResourceCreateNewGameTest {

    @BeforeEach
    public void beforeEach() {

    }

    @Test
    public void testCreateNewGame() {
        given()
                .when().post("/games")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .assertThat()
                .body("id", notNullValue())
                .body("url", notNullValue());
    }

}