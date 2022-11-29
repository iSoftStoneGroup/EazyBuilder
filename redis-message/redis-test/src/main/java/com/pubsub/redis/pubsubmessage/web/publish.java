package com.pubsub.redis.pubsubmessage.web;

import com.alibaba.fastjson.JSONObject;
import com.pubsub.redis.pubsubmessage.constant.RouteEnum;
import com.pubsub.redis.pubsubmessage.tools.RedisTool;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
 
@RestController
@RequestMapping
public class publish {

    @Resource(name = "redisTemplate")
    RedisTemplate redisTemplate;

    @GetMapping("pub")
    public void pub() {
        // 保存redis, 持久化
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "sfsfsdfsdfsdfsdfsdfsdfsdfsdf");
        jsonObject.put("checkUrl", "123");
        jsonObject.put("weight", "100");
        jsonObject.put("group", "xcx");
        jsonObject.put("path", "/xcx-web/**");
        jsonObject.put("uri", "http://192.168.0.104:8764");
        ArrayList<String> jsonList = new ArrayList<>();
        jsonList.add(jsonObject.toJSONString());
        RedisTool.setRightList(redisTemplate, RouteEnum.ROUTE_SAVE.getKey(), jsonList);
        redisTemplate.convertAndSend(RouteEnum.ROUTE_SAVE.getTopic(), jsonObject.toJSONString());
    }
}
