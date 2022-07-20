package com.eazybuilder.dm.service;

import org.springframework.amqp.core.Message;

public interface RabbitmqReturnCallbackService {
	public void excute(Message message, int replyCode, String replyText, String exchange, String routingKey);
}
