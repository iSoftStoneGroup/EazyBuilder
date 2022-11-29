package com.pubsub.redis.pubsubmessage.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

 
@Slf4j
@Service
public class RedisRouteReceiver {

    public void routeDeleteMessage(String message) {
        log.info("-------------》删除路由消息来了{}", message);

    }

    public void routeSaveMessage(String message) {
        log.info("-------------》保存路由消息来了{}", message);
    }

    public void routeUpdateMessage(String message) {
        log.info("-------------》更新路由消息来了{}", message);

    }
}
