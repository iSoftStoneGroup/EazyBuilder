package com.eazybuilder.dm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.eazybuilder.dm.constant.MsgType;
import com.eazybuilder.dm.entity.Team;
import com.eazybuilder.dm.entity.User;
import com.eazybuilder.dm.service.MailSenderHelper;
import com.eazybuilder.dm.service.MsgServiceImpl;
import com.eazybuilder.dm.service.TeamServiceImpl;
import com.eazybuilder.dm.service.UserServiceImpl;
import com.eazybuilder.dm.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.dm.util.DingtalkWebHookUtil;
import com.rabbitmq.client.Channel;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
public class ReceiveMsg {
	private static Logger logger = LoggerFactory.getLogger(ReceiveMsg.class);
	// 队列名称
//	@Value("${message.queue}")
//	public String ITEM_QUEUE;
	@Autowired
	private StringRedisTemplate redisTemplate;
	// 广播交换机名称
	@Value("${message.broadcastExchange}")
	public String broadcastExchange;
	@Value("${message.ciDataSyncExchange}")
	public String ciDataSyncExchange;

	@Value("${dingtalk.appKey}")
	public String appKey;
	@Value("${dingtalk.appSecret}")
	public String appSecret;
	@Value("${dingtalk.getTokenUrl}")
	public String getTokenUrl;
	@Value("${dingtalk.getUserIdUrl}")
	public String getUserIdUrl;
	@Value("${dingtalk.batchSendUrl}")
	public String batchSendUrl;
	@Value("${dingtalk.robotCode}")
	public String robotCode;
    @Value("${dingtalk.url}")
    public String dingtalkUrl;
//	@Bean
//	public Queue queue() {
//		return new Queue(ITEM_QUEUE, true);
//	}

	@Resource
	TeamServiceImpl teamService;
    @Resource
    MsgServiceImpl msgService;
	@Bean
	public Broadcast broadcast() {
		return new Broadcast(broadcastExchange);
	}

	@Bean
	public Broadcast broadcastCiDataSync() {
		return new Broadcast(ciDataSyncExchange);
	}



	public static void main(String[] args) throws Exception {
		String s = HttpUtil.get("https://oapi.dingtalk.com/gettoken?appkey=xxx");
		System.out.println(s);
	}
	@RabbitListener(bindings = {
			@QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue(value = "devops.initData.queue"), exchange = @Exchange(type = ExchangeTypes.FANOUT, name = "#{broadcastCiDataSync.exchange}")) })
	public void broadcastMsg(Message message, Channel channel) throws Exception {
		logger.info("接收到ci初始化消息");
		MessageHeaders headers = message.getHeaders();
		Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG); // 消息唯一标记
		String msgId = "";
		if (null != headers.get("spring_returned_message_correlation")) {
			msgId = headers.get("spring_returned_message_correlation").toString(); // 这里的消息ID即为生产端所生成的消息UUID
		}
		logger.info("消费者收到广播消息了：{},msgId:{},tag:{}", message.getPayload(), msgId, tag);
		// 当redis中存在此消息ID时,说明此消息已让消费过(用于幂等性的处理)
//		if (redisTemplate.opsForHash().entries("rancherMessageCache").containsKey(msgId) && !StringUtils.isEmpty(msgId)) {
//			logger.info("[{}]消息已消费过", msgId);
			channel.basicAck(tag, false); // 手动确认消息已消费
//			return;
//		}
		JSONObject jsonObject = (JSONObject) JSON.toJSON(message.getPayload());

		Team team = JSON.toJavaObject(jsonObject, Team.class);
		teamService.save(team);
	}

	/**
	 * 	 * @param message
	 * @param channel
	 * @throws Exception
	 */
	@RabbitListener(bindings = {
			@QueueBinding(value = @org.springframework.amqp.rabbit.annotation.Queue(value = "devops.dingtalk.queue"), exchange = @Exchange(type = ExchangeTypes.FANOUT, name = "#{broadcast.exchange}")) })
	public void ciDataSyncMsg(Message message, Channel channel) throws Exception {

		logger.info("接收到消息，准备发送消息提醒");
		MessageHeaders headers = message.getHeaders();
		Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG); // 消息唯一标记
		String msgId = "";
		if (null != headers.get("spring_returned_message_correlation")) {
			msgId = headers.get("spring_returned_message_correlation").toString(); // 这里的消息ID即为生产端所生成的消息UUID
		}
		logger.info("消费者收到广播消息了：{},msgId:{},tag:{}", message.getPayload(), msgId, tag);
		try {

			JSONObject messageJson = (JSONObject) JSON.toJSON(message.getPayload());
			boolean matchMsg = msgService.matchMsg(messageJson);
			logger.info("消息精准推送匹配结果：{}", matchMsg);
			if (matchMsg) {
				if (!messageJson.containsKey("msgType") || messageJson.getString("msgType").equals("ding")) {
					logger.info("mq中的类型为空或 ding，以钉钉消息处理");
					msgService.dingMsg(messageJson);
				} else if (messageJson.getString("msgType").equals("mail")) {
					logger.info("mq中的类型为 mail，以邮件消息处理");
					msgService.mailMsg(messageJson);
				}
			}
			logger.info("[{}]广播消息没有被消费过，继续后续流程", msgId);
			redisTemplate.opsForHash().put("rancherMessageCache", msgId, msgId);
		} finally {
			try {
				channel.basicAck(tag, false);
			} catch (IOException e) {
				logger.error("手动确认消息已消费出现异常:{}", e);
			}
		}
	}


	class Broadcast {
		private final String exchange;

		Broadcast(String exchange) {
			this.exchange = exchange;
		}

		public String getExchange() {
			return this.exchange;
		}
	}
}
