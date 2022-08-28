package com.bpa.backend.controller.user;

import com.bpa.backend.mapper.UserMapper;
import com.bpa.backend.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserMapper userMapper;

    @GetMapping("/all")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userMapper.selectById(userId);
    }
}
