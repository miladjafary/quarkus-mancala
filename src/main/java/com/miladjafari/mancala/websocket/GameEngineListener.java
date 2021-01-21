package com.miladjafari.mancala.websocket;

import com.miladjafari.mancala.dto.GameEvent;

public interface GameEngineListener {
    void onStart(GameEvent event);
    void onFinish(GameEvent event);
    void onBoardChange(GameEvent event);
}
