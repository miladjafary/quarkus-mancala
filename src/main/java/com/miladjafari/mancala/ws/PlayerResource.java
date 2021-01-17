package com.miladjafari.mancala.ws;

import com.miladjafari.mancala.sdk.exception.GameManagerException;
import com.miladjafari.mancala.service.GameManagerService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class PlayerResource {

    @Inject
    GameManagerService gameManagerService;

    @POST
    @Path("/games/{gameId}/{playerName}/pit/{pitId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response play(
            @PathParam("gameId") String gameId,
            @PathParam("playerName") String playerNames,
            @PathParam("pitId") Integer pit
    ) {
        try {
            gameManagerService.play(gameId, playerNames, pit);
            return Response.ok().build();
        } catch (GameManagerException exception) {
            JsonObject errorResponse = Json.createObjectBuilder()
                                           .add("error", exception.getMessage())
                                           .build();
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(errorResponse)
                           .build();
        }
    }

}