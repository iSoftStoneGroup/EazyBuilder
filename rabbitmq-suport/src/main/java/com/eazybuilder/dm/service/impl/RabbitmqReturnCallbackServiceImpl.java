package com.eazybuilder.dm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import com.eazybuilder.dm.service.RabbitmqReturnCallbackService;

@Service("defaultRabbitmqReturnCallbackService")
public class RabbitmqReturnCallbackServiceImpl implements RabbitmqReturnCallbackService {
	private static Logger logger = LoggerFactory.getLogger(RabbitmqReturnCallbackServiceImpl.class);

	public void excute(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		logger.error("消息丢失，不能路由({}),message:{},replyCode:{},replyText:{},routingKey:{}", exchange, message, replyCode,
				replyText, routingKey);
	}

}
