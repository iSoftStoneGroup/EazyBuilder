package com.eazybuilder.ga.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ga.component.RequestComponent;
import com.eazybuilder.ga.pojo.CodePojo;
import com.eazybuilder.ga.service.CheckService;
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
public class CheckServiceImpl implements CheckService {
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
    @Value("${redmine.checkCode}")
    public String ROUTING_KEY;

    @Override
    public String numberCheck(String note, String userName) {
        String code = "";

        //0.字符串处理，去除去除字符串中的空格、换行等转义字符
        String newNote = Pattern.compile("\t|\r|\n| ").matcher(note).replaceAll("");
        //1.校验注释长度，大于20个字节
        if (newNote.length() < 20) {
            return "注释长度，小于20个字节";
        }
        //2.代码提交注释规范
        String pattern = "(\\[ADD\\]|\\[REM\\]|\\[IMP\\]|\\[FIX\\]|\\[REF\\])[A-Z]*[-|#]([0-9]+).+";
        boolean isMatch = Pattern.matches(pattern, newNote);
        if (isMatch) {
            Pattern r = Pattern.compile(pattern);
            // 现在创建 matcher 对象
            Matcher m = r.matcher(newNote);
            if (m.find()) {
                code = m.group(2);
            } else {
                return "代码提交注释规范";
            }
        } else {
            return "代码提交注释规范";
        }
        //3.注释中包含ID,查询需求号是否合法：在redmine中的状态位是开发进行中。
        UUID correlationDataId = UUID.randomUUID();
        logger.info("发送消息，msgId:{}", correlationDataId);
        CorrelationData correlationData = new CorrelationData(correlationDataId.toString());
        CodePojo codePojo= new CodePojo();
        codePojo.setCode(code);
        codePojo.setMessage(note);
        codePojo.setUserName(userName);
        //1.怎么把返回的数据添加到reponse中
        //2.这里如何保证在消息队列里取到的是这次处理的结果
        Object convertSendAndReceiveResult = rabbitTemplate.convertSendAndReceive(ITEM_TOPIC_EXCHANGE, ROUTING_KEY, JSONObject.toJSONString(codePojo), correlationData);
        if (convertSendAndReceiveResult == null){
            return "需求号校验失败。无法请求redmine服务器";
        }
        JSONObject createRes = JSONObject.parseObject(convertSendAndReceiveResult.toString());
        Boolean data = createRes.getBooleanValue("flag");
        if (data != null) {//请求出现问题
            if (data){
                return "成功";
            }
            return createRes.getString("message");
        }else{
            return "需求号校验失败。无法从redmin服务器中获取数据";
        }

    }


}
