package com.miladjafari.mancala.dto;

import javax.json.Json;

/**
 * This class is used to publish game event to clients.
 */
public class GameEvent {
    private static final String GAME_STARTED = "GAME_STARTED";
    private static final String GAME_BOARD_CHANGE = "GAME_BOARD_CHANGE";
    private static final String GAME_OVER = "GAME_OVER";

    /**
     * Id of the game that event should be published for it
     */
    private String gameId = "";

    /**
     * Game Event. Possible values
     * <ul>
     *     <li>{@link GameEvent#GAME_STARTED}</li>
     *     <li>{@link GameEvent#GAME_BOARD_CHANGE}</li>
     *     <li>{@link GameEvent#GAME_OVER}</li>
     * </ul>
     */
    private String event = "";

    public static Builder builder() {
        return new Builder();
    }

    public String getGameId() {
        return gameId;
    }

    public String getEvent() {
        return event;
    }

    public String toJson() {
        return Json.createObjectBuilder()
                   .add("gameId", gameId)
                   .add("event", event)
                   .build()
                   .toString();
    }

    public static class Builder {
        private final GameEvent instance = new GameEvent();

        public Builder gameId(String gameId) {
            instance.gameId = gameId;
            return this;
        }

        public Builder gameStarted() {
            instance.event = GAME_STARTED;
            return this;
        }

        public Builder gameBoardChange() {
            instance.event = GAME_BOARD_CHANGE;
            return this;
        }

        public Builder gameOver() {
            instance.event = GAME_OVER;
            return this;
        }

        public GameEvent build() {
            return instance;
        }
    }
}
