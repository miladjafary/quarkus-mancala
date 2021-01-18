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
import java.util.logging.Logger;

@Path("/")
public class GameResource {

    private static final Logger logger = Logger.getLogger(GameManagerService.class.getName());

    @Inject
    GameManagerService gameManagerService;

    @POST
    @Path("/games")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNew(@Context UriInfo uriInfo) {
        String gameId = gameManagerService.createNewRoom();
        JsonObject response = Json.createObjectBuilder()
                                  .add("id", gameId)
                                  .add("uri", uriInfo.getBaseUri() + "games/" + gameId)
                                  .build();
        return Response.created(uriInfo.getBaseUri()).entity(response).build();
    }

    @POST
    @Path("/games/{gameId}/addPlayer/{playerName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlayer(
            @PathParam("gameId") String gameId,
            @PathParam("playerName") String playerNames
    ) {
        try {
            gameManagerService.addPlayer(gameId, playerNames);
            return Response.ok().build();
        } catch (GameManagerException exception) {
            logger.severe(exception.getMessage());
            JsonObject errorResponse = Json.createObjectBuilder()
                                           .add("error", exception.getMessage())
                                           .build();
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(errorResponse)
                           .build();
        }
    }
}