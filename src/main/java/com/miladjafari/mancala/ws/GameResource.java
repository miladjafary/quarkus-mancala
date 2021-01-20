package com.miladjafari.mancala.ws;

import com.miladjafari.mancala.dto.GameInfo;
import com.miladjafari.mancala.sdk.exception.GameManagerException;
import com.miladjafari.mancala.service.GameManagerService;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Map;
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
                                  .add("url", uriInfo.getBaseUri() + "games/" + gameId)
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

    @PUT
    @Path("/games/{gameId}/{playerName}/pit/{pitId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response play(
            @Context UriInfo uriInfo,
            @PathParam("gameId") String gameId,
            @PathParam("playerName") String playerNames,
            @PathParam("pitId") Integer pit
    ) {
        try {
            Map<String, Integer> gameBoardStatus = gameManagerService.play(gameId, playerNames, pit);
            JsonObject response = Json.createObjectBuilder()
                                      .add("id", gameId)
                                      .add("uri", uriInfo.getBaseUri() + "games/" + gameId)
                                      .add("status", toJson(gameBoardStatus))
                                      .build();

            return Response.ok(response).build();
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

    private JsonObjectBuilder toJson(Map<String, Integer> gameBoardStatus) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        gameBoardStatus.forEach(jsonObjectBuilder::add);

        return jsonObjectBuilder;
    }

    @GET
    @Path("/games/{gameId}/{playerName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameInfo(
            @PathParam("gameId") String gameId,
            @PathParam("playerName") String playerNames
    ) {
        try {
            GameInfo gameInfo = gameManagerService.getGameInfo(gameId, playerNames);
            return Response.ok(gameInfo).build();
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