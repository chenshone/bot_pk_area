package com.bpa.backend.server.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bpa.backend.mapper.UserMapper;
import com.bpa.backend.pojo.User;
import com.bpa.backend.server.user.account.RegisterService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if (username == null || username.trim().length() == 0) {
            map.put("message", "用户名不能为空");
            return map;
        }
        if (password == null || password.length() == 0 || confirmedPassword == null || confirmedPassword.length() == 0) {
            map.put("message", "密码不能为空");
            return map;
        }
        username = username.trim();

        if (username.length() > 100) {
            map.put("message", "用户名不能超过100");
            return map;
        }
        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("message", "密码不能超过100");
            return map;
        }
        if (!password.equals(confirmedPassword)) {
            map.put("message", "两次密码不一致");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> userList = userMapper.selectList(queryWrapper);

        if (!userList.isEmpty()) {
            map.put("message", "用户名已存在");
            return map;
        }

        String encode = passwordEncoder.encode(password);
        String avatar = "https://cdn.jsdelivr.net/gh/chenshone/myPictureHost@main/code_class/avatar.jpg";

        User user = new User(null, username, encode, avatar);
        userMapper.insert(user);
        map.put("message", "success");

        return map;
    }
}
