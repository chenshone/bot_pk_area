package com.bpa.backend.server.impl.pk;

import com.bpa.backend.consumer.WebSocketServer;
import com.bpa.backend.server.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer bId) {
        System.out.println("start game aId: " + aId + " bId " + bId);
        WebSocketServer.startGame(aId, bId);
        return "start game aId: " + aId + " bId " + bId;
    }
}
