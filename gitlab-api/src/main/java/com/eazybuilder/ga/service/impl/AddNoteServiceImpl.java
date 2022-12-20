package com.eazybuilder.ga.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.component.RequestComponent;
import com.eazybuilder.ga.pojo.CodePojo;
import com.eazybuilder.ga.service.AddNoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddNoteServiceImpl implements AddNoteService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RequestComponent requestComponent;

    // 注入RabbitMQ的模板
    @Autowired
    @Qualifier(value = "eazybuilderRabbitTemplate")
    private RabbitTemplate rabbitTemplate;
    // 交换机名称
    @Value("${redmine.topicExchange}")
    public String ITEM_TOPIC_EXCHANGE;

    // 交换机名称
    @Value("${redmine.addNote}")
    public String ROUTING_KEY;

    @Override
    public void addNote(String note, String userName, String code) {
        UUID correlationDataId = UUID.randomUUID();
        logger.info("发送消息，msgId:{}", correlationDataId);
        CorrelationData correlationData = new CorrelationData(correlationDataId.toString());
        CodePojo codePojo= new CodePojo();
        codePojo.setCode(code);
        codePojo.setMessage(note);
        codePojo.setUserName(userName);
        //1.怎么把返回的数据添加到reponse中
        //2.这里如何保证在消息队列里取到的是这次处理的结果
        rabbitTemplate.convertAndSend(ITEM_TOPIC_EXCHANGE, ROUTING_KEY, JSONObject.toJSONString(codePojo), correlationData);
    }
}
