package com.bpa.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pk")
public class BotInfoController {

    @RequestMapping("/botinfo")
    public Map<String,String> getBotInfo(){
        HashMap<String, String> bot1 = new HashMap<>();
        bot1.put("name", "cxt");
        bot1.put("rating", "1500");
        return bot1;
    }
}
