package com.bpa.backend.server.impl.user.bot;

import com.bpa.backend.mapper.BotMapper;
import com.bpa.backend.pojo.Bot;
import com.bpa.backend.pojo.User;
import com.bpa.backend.server.impl.utils.UserDetailsImpl;
import com.bpa.backend.server.user.bot.AddService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Resource
    BotMapper botMapper;

    @Override
    public Map<String, String> add(Map<String, String> data) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserDetailsImpl principal = (UserDetailsImpl) securityContext.getAuthentication().getPrincipal();
        User user = principal.getUser();

        Map<String, String> map = new HashMap<>();

        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        if (title == null || title.length() == 0) {
            map.put("message", "标题不能为空");
            return map;
        }
        if (title.length() > 100) {
            map.put("message", "标题长度不能大于100");
            return map;
        }
        if (description == null || description.length() == 0) {
            description = "这个用户很懒，描述也不写~";
        }
        if (description.length() > 300) {
            map.put("message", "描述长度不能大于300");
            return map;
        }
        if (content == null || content.length() == 0) {
            map.put("message", "bot代码不能为空");
            return map;
        }
        if (content.length() > 100000) {
            map.put("message", "bot代码长度不能大于100000");
            return map;
        }

        Date now = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, content, now, now);

        botMapper.insert(bot);
        map.put("message", "success");

        return map;
    }
}
