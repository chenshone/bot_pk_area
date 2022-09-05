package com.bpa.backend.server.record;

import com.alibaba.fastjson.JSONObject;

public interface GetRecordListService {
    JSONObject getList(Integer page);
}
