package com.eazybuilder.ga.service;

import com.alibaba.fastjson.JSON;
import com.eazybuilder.ga.pojo.InitResultPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SendInitMessage {

    private static Logger logger = LoggerFactory.getLogger(SendInitMessage.class);

    // 注入RabbitMQ的模板
    @Autowired
    @Qualifier(value = "eazybuilderRabbitTemplate")
    private RabbitTemplate rabbitTemplate;
    // 交换机名称
    @Value("${message.initResultBroadcastExchange}")
    public String initResultBroadcastExchange;

    public String sendInitResultMessage(InitResultPojo data) {
        /**
         * 发送消息 参数一：交换机名称 参数二：路由key: rancher.#,符合路由rancher.#规则即可 参数三：发送的消息
         */
        try {
            UUID correlationDataId = UUID.randomUUID();
            String msg = JSON.toJSONString(data);
            logger.info("发送消息，msgId:{}.msgData:{}", correlationDataId, msg);

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("text/json");
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            rabbitTemplate.convertAndSend(initResultBroadcastExchange,"rancher.#", message);
            // 返回消息
            return "success send message to " + initResultBroadcastExchange;
        } catch (Exception e) {
            logger.error("CI平台(jenkins-client)发送消息异常:{}", e.getStackTrace());
        }

        return null;
    }

}
