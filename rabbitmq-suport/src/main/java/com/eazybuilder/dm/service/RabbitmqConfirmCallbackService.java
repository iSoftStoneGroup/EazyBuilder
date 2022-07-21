package com.eazybuilder.dm.service;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.lang.Nullable;

public interface RabbitmqConfirmCallbackService {
	public void excute(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause);
}
