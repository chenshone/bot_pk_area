package com.bpa.backend.server.impl.user.bot;

import com.bpa.backend.mapper.BotMapper;
import com.bpa.backend.pojo.Bot;
import com.bpa.backend.pojo.User;
import com.bpa.backend.server.impl.utils.UserDetailsImpl;
import com.bpa.backend.server.user.bot.RemoveService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Resource
    BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        Map<String, String> map = new HashMap<>();
        UserDetailsImpl principal =
                (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();

        int bot_id = Integer.parseInt(data.get("id"));

        Bot bot = botMapper.selectById(bot_id);

        if (bot == null) {
            map.put("message", "bot不存在或已删除");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())) {
            map.put("message", "无权删除他人的bot");
            return map;
        }

        botMapper.deleteById(bot_id);
        map.put("message", "success");
        return map;
    }
}
