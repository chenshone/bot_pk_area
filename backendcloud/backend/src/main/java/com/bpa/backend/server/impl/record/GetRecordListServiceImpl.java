package com.bpa.backend.server.impl.record;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bpa.backend.mapper.RecordMapper;
import com.bpa.backend.mapper.UserMapper;
import com.bpa.backend.pojo.Record;
import com.bpa.backend.pojo.User;
import com.bpa.backend.server.record.GetRecordListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Resource
    private RecordMapper recordMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<Record> recordPage = new Page<>(page, 10);
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.orderByDesc("id");
        List<Record> recordList = recordMapper.selectPage(recordPage, recordQueryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> list = new LinkedList<>();
        for (Record record : recordList) {
            User userA = userMapper.selectById(record.getAId());
            User userB = userMapper.selectById(record.getBId());
            JSONObject item = new JSONObject();
            item.put("a_avatar", userA.getAvatar());
            item.put("a_username", userA.getUsername());
            item.put("b_avatar", userB.getAvatar());
            item.put("b_username", userB.getUsername());
            String result = "平局";
            if ("A".equals(record.getLoser())) result = "B胜";
            else if ("B".equals(record.getLoser())) result = "A胜";
            item.put("result", result);
            item.put("record", record);
            list.add(item);
        }
        resp.put("records", list);
        resp.put("records_count", recordMapper.selectCount(null));

        return resp;
    }
}
