package com.eazybuilder.ga.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.eazybuilder.dm.service.RabbitmqConfirmCallbackService;

@Service("issRabbitmqConfirmCallbackService")
public class IssRabbitmqConfirmCallbackServiceImpl implements RabbitmqConfirmCallbackService {
	private static Logger logger = LoggerFactory.getLogger(IssRabbitmqConfirmCallbackServiceImpl.class);
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void excute(CorrelationData correlationData, boolean ack, String cause) {
		if (ack) {
			logger.info("消息成功发送到路由器，等待消费方处理:{}", correlationData != null ? correlationData.getId() : "correlationData为空，id为空");

		} else {
			logger.error("消息发送异常，cause:{},correlationDataID:{}", cause,
					correlationData != null ? correlationData.getId() : "correlationData为空，id为空");
		}

	}

}
