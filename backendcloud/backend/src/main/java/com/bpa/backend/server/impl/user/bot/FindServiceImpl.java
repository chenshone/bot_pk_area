package com.bpa.backend.server.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bpa.backend.mapper.BotMapper;
import com.bpa.backend.pojo.Bot;
import com.bpa.backend.pojo.User;
import com.bpa.backend.server.impl.utils.UserDetailsImpl;
import com.bpa.backend.server.user.bot.FindService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FindServiceImpl implements FindService {
    @Resource
    BotMapper botMapper;

    @Override
    public List<Bot> getList() {
        UserDetailsImpl principal =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        return botMapper.selectList(queryWrapper);
    }
}
