package com.bpa.matchingsystem.service.impl;

import com.bpa.matchingsystem.service.MatchingService;
import com.bpa.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("add player" + userId + " rating " + rating + " bot id " + botId);
        matchingPool.addPlayer(userId, rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player " + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}
