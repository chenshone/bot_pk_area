package com.bpa.botrunningsystem.service.impl.utils;

import com.bpa.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.UUID;

@Component
public class Consumer extends Thread {
    private static final String receiveBotUrl = "http://127.0.0.1:8090/pk/receive/bot/move";
    private static RestTemplate restTemplate;
    private Bot bot;

    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }

    public void startTimeout(long timeout, Bot bot) {
        this.bot = bot;
        this.start();
        try {
            this.join(timeout); // 阻塞当前线程最多timeout时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt(); // 结束当前线程
        }
    }

    // 在code的bot名后面加上uid
    private String addUid(String code, String uid) {
        int k = code.indexOf(" implements com.bpa.botrunningsystem.utils.BotInterface");
        return code.substring(0, k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);
//        编译bot代码
        BotInterface botInterface =
                Reflect.compile("com.bpa.botrunningsystem.utils.Bot" + uid, addUid(bot.getBotCode(), uid)).create().get();
        Integer direction = botInterface.nextStep(bot.getInput());
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());
        String resp = restTemplate.postForObject(receiveBotUrl, data, String.class);
        System.out.println(resp);
    }
}
