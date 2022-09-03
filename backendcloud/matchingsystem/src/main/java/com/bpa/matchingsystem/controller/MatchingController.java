package com.bpa.matchingsystem.controller;

import com.bpa.matchingsystem.service.MatchingService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/player")
public class MatchingController {
    @Resource
    private MatchingService matchingService;

    @PostMapping("/add")
    public String addPlayer(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating  = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        return matchingService.addPlayer(userId,rating);
    }

    @PostMapping("/remove")
    public String removePlayer(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingService.removePlayer(userId);
    }
}
