package com.miladjafari.mancala.ws;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

public class AbstractGameIT {
    public String createNewGame() {
        return given()
                .when().post("/games")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract()
                .path("id");
    }

    public void addPlayer(String gameId, String player) {
        given()
                .when().post(String.format("/games/%s/addPlayer/%s", gameId, player))
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
