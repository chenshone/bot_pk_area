package com.bpa.backend.controller.record;


import com.alibaba.fastjson.JSONObject;
import com.bpa.backend.server.record.GetRecordListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/record")
public class GetRecordListController {
    @Resource
    private GetRecordListService getRecordListService;

    @GetMapping("/getlist")
    public JSONObject getList(@RequestParam Map<String, String> data) {
        Integer page = Integer.parseInt(data.get("page"));
        return getRecordListService.getList(page);
    }
}
