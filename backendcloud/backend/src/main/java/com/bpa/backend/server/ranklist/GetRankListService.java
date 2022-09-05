package com.bpa.backend.server.ranklist;

import com.alibaba.fastjson.JSONObject;

public interface GetRankListService {
    JSONObject getList(Integer page);
}
