package com.bpa.backend.server.impl.pk;

import com.bpa.backend.consumer.WebSocketServer;
import com.bpa.backend.server.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBOtId) {
        System.out.println("start game aId: " + aId + " a bot id " + aBotId + " bId " + bId + " b bot id " + bBOtId);
        WebSocketServer.startGame(aId, aBotId, bId, bBOtId);
        return "start game aId: " + aId + " a bot id " + aBotId + " bId " + bId + " b bot id " + bBOtId;
    }
}
