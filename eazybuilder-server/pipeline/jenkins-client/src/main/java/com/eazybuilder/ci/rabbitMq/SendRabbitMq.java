package com.eazybuilder.ci.rabbitMq;


import com.alibaba.fastjson.JSONArray;
import com.eazybuilder.ci.dto.IssuesStatusDto;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.entity.devops.IssuesStatus;
import com.eazybuilder.ci.util.AuthUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class SendRabbitMq {
    private static Logger logger = LoggerFactory.getLogger(SendRabbitMq.class);


    // 注入RabbitMQ的模板
    @Resource
    @Qualifier(value = "issRabbitTemplate")
    private RabbitTemplate rabbitTemplate;
    // 交换机名称 广播形式
    @Value("${message.fanoutExchange}")
    public String fanoutExchange;
    @Value("${message.routingKey}")
    public String routingKey;
    @Value("${message.ciStatusExchange}")
    public String ciStatusExchange;
    
    @Value("${message.issNotifyFanoutExchange}")
    public String issNotifyFanoutExchange;
    

    /**
     * 有的service 没有被spring 托管，无法通过@value 获得转换器和key的值
     *
     * @param msg
     * @return
     */
    public String sendMsg(String msg) {
        /**
         * 发送消息 参数一：交换机名称 参数二：路由key: rancher.#,符合路由rancher.#规则即可 参数三：发送的消息
         */
        try {
            UUID correlationDataId = UUID.randomUUID();
            logger.info("发送消息，msgId:{}.msgData:{}", correlationDataId, msg);
            //ci 用的 jar包里没用这个类，暂时注释掉
//            CorrelationData correlationData = new CorrelationData(correlationDataId.toString());

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("text/json");
            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            rabbitTemplate.convertAndSend(fanoutExchange, routingKey, message);
            // 返回消息
            return "success send message to " + fanoutExchange;
        } catch (Exception e) {
            logger.error("CI平台(jenkins-client)发送消息异常:{}", e.getStackTrace());
        }

        return null;
    }

    /**
     * 由调用方传exchange，routingKey
     * @param msg
     * @param exchange
     * @param routingKey
     */
    public String sendMsg(String msg, String exchange, String routingKey) {
        /**
         * 发送消息 参数一：交换机名称 参数二：路由key: rancher.#,符合路由rancher.#规则即可 参数三：发送的消息
         */
        try {
            UUID correlationDataId = UUID.randomUUID();
            logger.info("ci 发送消息，msgId:{}.msgData:{}", correlationDataId, msg);

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("text/json");

            Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            // 返回消息
            return "success send message to " + exchange;
        } catch (Exception e) {
            logger.error("CI平台(jenkins-client)发送消息异常:{}", e.getStackTrace());
        }

        return null;
    }


    public void sendReleaseStatusMq(String issuesIds, IssuesStatus issuesStatus){
        if(StringUtils.isNotBlank(issuesIds)) {
            JSONArray data = new JSONArray();
            User user = AuthUtils.getCurrentUser();
            if(issuesIds.contains(",")){
                String[] issuesId = issuesIds.split(",");
                for(String code:issuesId){
                    IssuesStatusDto issuesStatusDto = new IssuesStatusDto();
                    issuesStatusDto.setCode(code);
                    issuesStatusDto.setIssuesStatus(issuesStatus.getName());
                    if(null!=user) {
                        issuesStatusDto.setUserName(user.getEmail().split("@")[0]);
                    }else{
                        issuesStatusDto.setUserName("kunyangu");
                    }
                    data.add(issuesStatusDto);
                }
            }else{
                IssuesStatusDto issuesStatusDto = new IssuesStatusDto();
                issuesStatusDto.setCode(issuesIds);
                issuesStatusDto.setIssuesStatus(issuesStatus.getName());
                if(null!=user) {
                    issuesStatusDto.setUserName(user.getEmail().split("@")[0]);
                }else{
                    issuesStatusDto.setUserName("kunyangu");
                }
                data.add(issuesStatusDto);
            }
            logger.info("申请提测时发送mq 修改需求状态");
            sendMsg(data.toString(),ciStatusExchange,null);
        }
    }
    
	public void publish(String msg) {
		/**
		 * 发送消息 参数一：交换机名称 参数二：路由key: rancher.#,符合路由rancher.#规则即可 参数三：发送的消息
		 */

		UUID correlationDataId = UUID.randomUUID();
		logger.info("发送广播，msgId:{}", correlationDataId);
        logger.info("ci发送通过mq发送通知消息，msg:{}", msg);
	    MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("text/json");

        Message message = new Message(msg.getBytes(StandardCharsets.UTF_8), messageProperties);
		rabbitTemplate.convertAndSend(issNotifyFanoutExchange, "", message);
		 
	}
    

}
