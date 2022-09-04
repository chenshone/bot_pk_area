package com.bpa.botrunningsystem.service;

public interface BotRunningService {
    /**
     * @param userId
     * @param botCode
     * @param input   当前对局的信息，比如地图信息，蛇的信息等等
     * @return
     */
    String addBot(Integer userId, String botCode, String input);
}
