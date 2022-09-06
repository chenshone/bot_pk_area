package com.bpa.backend.controller.user.bot;

import com.bpa.backend.server.user.bot.RemoveService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/user/bot")
public class RemoveController {
    @Resource
    RemoveService removeService;

    @PostMapping("/remove")
    public Map<String, String> remove(@RequestParam Map<String, String> data) {
        return removeService.remove(data);
    }
}
