package com.miladjafari.mancala.ws;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class PlayResourceTest extends AbstractGameIT {

    private String gameId;
    private String player1 = "milad";
    private String player2 = "Elena";

    @BeforeEach
    public void beforeEach() {
        gameId = createNewGame();
        addPlayer(gameId, player1);
        addPlayer(gameId, player2);
    }

    private String createPlayUrl(String gameId, String player, String selectedPit) {
        return String.format("/games/%s/%s/pits/%s", gameId, player, selectedPit);
    }

    @Test
    @Disabled
    public void testSuccessPlay() {
        given()
                .when().post(createPlayUrl(gameId, player1, "1"))
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .assertThat()
                .body("id", notNullValue())
                .body("uri", notNullValue());
    }
}