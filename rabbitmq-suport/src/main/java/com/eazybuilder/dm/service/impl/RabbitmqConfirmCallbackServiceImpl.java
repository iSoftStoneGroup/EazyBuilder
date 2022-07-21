package com.eazybuilder.dm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import com.eazybuilder.dm.service.RabbitmqConfirmCallbackService;

@Service("defaultRabbitmqConfirmCallbackService")
public class RabbitmqConfirmCallbackServiceImpl implements RabbitmqConfirmCallbackService {
	private static Logger logger = LoggerFactory.getLogger(RabbitmqConfirmCallbackServiceImpl.class);

	public void excute(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
		if (ack) {
			logger.info("消息成功发送到路由器，correlationDataID:{}", correlationData != null ? correlationData.getId() : "correlationData为空，id为空");
			// 幂等性处理，防止重复消费
		} else {
			logger.error("消息发送异常，cause:{},correlationDataID:{}", cause,
					correlationData != null ? correlationData.getId() : "correlationData为空，id为空");
		}
	}
}
