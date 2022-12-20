package com.eazybuilder.ga.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.component.RequestComponent;
import com.eazybuilder.ga.pojo.CIPackagePojo;
import com.eazybuilder.ga.service.CIPackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Service
public class CIPackageServiceImpl implements CIPackageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RequestComponent requestComponent;

    // 注入RabbitMQ的模板
    @Autowired
    @Qualifier(value = "eazybuilderRabbitTemplate")
    private RabbitTemplate rabbitTemplate;
    // 交换机名称
    @Value("${ci.topicExchange}")
    public String ITEM_TOPIC_EXCHANGE;

    @Value("${ci.package}")
    public String ROUTING_KEY;

    @Override
    public void sendCIPackage(CIPackagePojo data) {

        UUID correlationDataId = UUID.randomUUID();
        logger.info("发送消息，msgId:{}", correlationDataId);
        CorrelationData correlationData = new CorrelationData(correlationDataId.toString());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/json");
        String msg = JSONObject.toJSONString(data);
        Message message = null;
        try {
            message = new Message(msg.getBytes("UTF-8"), messageProperties);
            //1.怎么把返回的数据添加到reponse中
            //2.这里如何保证在消息队列里取到的是这次处理的结果
            rabbitTemplate.convertAndSend(ITEM_TOPIC_EXCHANGE, ROUTING_KEY, message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



    }
}
