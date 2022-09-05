package com.bpa.backend.server.impl.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bpa.backend.mapper.UserMapper;
import com.bpa.backend.pojo.User;
import com.bpa.backend.server.ranklist.GetRankListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GetRankListServiceImpl implements GetRankListService {
    @Resource
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<User> userPage = new Page<>(page, 3);
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.orderByDesc("rating");
        List<User> userList = userMapper.selectPage(userPage, userQueryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        for (User user : userList) {
            user.setPassword("");
        }
        resp.put("users", userList);
        resp.put("users_count", userMapper.selectCount(null));

        return resp;
    }
}
