package com.bpa.backend.controller.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.bpa.backend.server.ranklist.GetRankListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/ranklist")
public class GetRankListController {
    @Resource
    private GetRankListService getRankListService;

    @GetMapping("/getlist")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        return getRankListService.getList(page);
    }
}
