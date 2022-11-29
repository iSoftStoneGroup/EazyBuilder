package com.pubsub.redis.pubsubmessage.config;

import com.pubsub.redis.pubsubmessage.constant.RouteEnum;
import com.pubsub.redis.pubsubmessage.receiver.RedisRouteReceiver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * @Version: 0.0.1V
 * @Date: 2018/5/9
 * @Description: 类描述
 **/
@Slf4j
@Component
public class RouteRedisConfig {

    //使用默认的工厂初始化redis操作模板
    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    /**
     * 初始化监听器
     *
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerRouteSaveAdapter,
                                            MessageListenerAdapter listenerRouteDeleteAdapter,
                                            MessageListenerAdapter listenerRouteUpdateAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 监听保存路由
        container.addMessageListener(listenerRouteSaveAdapter, new PatternTopic(RouteEnum.ROUTE_SAVE.getTopic()));
        // 监听删除路由
        container.addMessageListener(listenerRouteDeleteAdapter, new PatternTopic(RouteEnum.ROUTE_DELETE.getTopic()));
        // 监听更新路由
        container.addMessageListener(listenerRouteUpdateAdapter, new PatternTopic(RouteEnum.ROUTE_UPDATE.getTopic()));
        return container;
    }

    /**
     * 监听保存路由消息后处理
     *
     * @return
     */
    @Bean
    MessageListenerAdapter listenerRouteSaveAdapter(RedisRouteReceiver redisRouteSaveReceiver) {
        log.info("-----------》保存路由消息适配器执行");
        return new MessageListenerAdapter(redisRouteSaveReceiver, "routeSaveMessage");
    }

    /**
     * 监听删除路由消息后处理
     *
     * @return
     */
    @Bean
    MessageListenerAdapter listenerRouteDeleteAdapter(RedisRouteReceiver redisRouteDeleteReceiver) {
        log.info("-----------》删除路由消息适配器执行");
        return new MessageListenerAdapter(redisRouteDeleteReceiver, "routeDeleteMessage");
    }

    /**
     * 监听更新路由消息后处理
     *
     * @return
     */
    @Bean
    MessageListenerAdapter listenerRouteUpdateAdapter(RedisRouteReceiver redisRouteUpdateReceiver) {
        log.info("-----------》更新路由消息适配器执行");
        return new MessageListenerAdapter(redisRouteUpdateReceiver, "routeUpdateMessage");
    }
}
