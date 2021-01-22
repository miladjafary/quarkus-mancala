package com.miladjafari.mancala.websocket;


import com.miladjafari.mancala.dto.GameEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * This class is responsible to manges websockets between client and server.
 * It can broad cast different {@link GameEvent} (such as GAME_STARTED, GAME_OVER, GAME_BOARD_CHANGE) to the client or
 * game players browsers.
 */
@ServerEndpoint(value = "/gameNotifier")
@ApplicationScoped
public class GameNotifier implements GameEngineListener {

    private static final Logger logger = Logger.getLogger(GameNotifier.class.getName());

    private final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        logger.info("WebSocket opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("WebSocket closed: " + session.getId());
        sessions.remove(session);
    }

    @Override
    public void onStart(GameEvent event) {
        logger.info("Broadcasting:" + event.getEvent());
        broadcast(event.toJson());
    }

    @Override
    public void onFinish(GameEvent event) {
        logger.info("Broadcasting:" + event.getEvent());
        broadcast(event.toJson());
    }

    @Override
    public void onBoardChange(GameEvent event) {
        logger.info("Broadcasting:" + event.getEvent());
        broadcast(event.toJson());
    }

    public void broadcast(String message) {
        sessions.forEach(session -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.severe("Error in sending message");
            }
        });
    }
}
