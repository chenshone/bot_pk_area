package com.bpa.backend.controller.user.bot;

import com.bpa.backend.pojo.Bot;
import com.bpa.backend.server.user.bot.FindService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/user/bot")
public class FindController {
    @Resource
    FindService findService;

    @GetMapping("/get-list")
    public List<Bot> getList() {
        return findService.getList();
    }
}
