package com.eazybuilder.ga.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.component.RequestComponent;
import com.eazybuilder.ga.pojo.ChangeStatusPojo;
import com.eazybuilder.ga.service.ChangeStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ChangeStatusServiceImpl implements ChangeStatusService {

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
    @Value("${redmine.changeStatus}")
    public String ROUTING_KEY;

    @Override
    public String changeStatus(String note, String userName, Boolean successFlag) {
        UUID correlationDataId = UUID.randomUUID();
        logger.info("发送消息，msgId:{}", correlationDataId);
        CorrelationData correlationData = new CorrelationData(correlationDataId.toString());

        String patternStr = "[A-Za-z]+-([0-9]+)";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(note);
        ChangeStatusPojo changeStatusPojo = new ChangeStatusPojo();

        if (matcher.find()) {
            String newNotes = matcher.group(0);
            String arr[] = newNotes.split("-");
            String code = arr[1];

            changeStatusPojo.setCode(code);
            changeStatusPojo.setUserName(userName);
            changeStatusPojo.setSuccessFlag(successFlag);
            changeStatusPojo.setMessage(note);

        }else{
            return "非法分支名称";
        }
        Object convertSendAndReceiveResult = rabbitTemplate.convertSendAndReceive(ITEM_TOPIC_EXCHANGE, ROUTING_KEY, JSONObject.toJSONString(changeStatusPojo), correlationData);
        if (convertSendAndReceiveResult == null){
            return "状态更新失败,无法请求redmine服务器";
        }
        JSONObject createRes = JSONObject.parseObject(convertSendAndReceiveResult.toString());
        Boolean data = createRes.getBooleanValue("flag");
        if (data != null) {//请求出现问题
            if (data){
                return "成功";
            }
            return createRes.getString("message");
        }else{
            return "状态更新失败,无法从redmin服务器中获取数据";
        }
    }
}
