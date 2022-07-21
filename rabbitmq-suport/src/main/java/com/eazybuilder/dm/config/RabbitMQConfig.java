package com.eazybuilder.dm.config;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

/**
 * RabbitMQ配置类
 */
@Configuration
public class RabbitMQConfig {
	private static Logger logger = LoggerFactory.getLogger(RabbitMQConfig.class);
	// 交换机名称
	@Value("${message.topicExchange}")
	public String ITEM_TOPIC_EXCHANGE;
	// 队列名称
	@Value("${message.queue}")
	public String ITEM_QUEUE;
	// 路由key
	@Value("${message.routingKey}")
	public String routingKey;

	@Autowired
	private CachingConnectionFactory cachingConnectionFactory;

	@Value("${message.confirmCallbackBean:defaultRabbitmqConfirmCallbackService}")
	private String confirmCallbackBean;
	@Value("${message.returnCallbackBean:defaultRabbitmqReturnCallbackService}")
	private String returnCallbackBean;

	// 声明交换机
	@Bean("eazybuilderTopicExchange")
	public Exchange topicExchange() {
		logger.info("绑定消息路由:{}", ITEM_TOPIC_EXCHANGE);
		return ExchangeBuilder.topicExchange(ITEM_TOPIC_EXCHANGE).durable(true).build();
	}

	// 声明队列
	@Bean("eazybuilderQueue")
	public Queue itemQueue() {
		logger.info("绑定消息队列:{}", ITEM_QUEUE);
		return QueueBuilder.durable(ITEM_QUEUE).build();
	}

	// 绑定队列和交换机
	@Bean
	public Binding itemQueueExchange(@Qualifier("eazybuilderQueue") Queue queue,
			@Qualifier("eazybuilderTopicExchange") Exchange exchange) {
		logger.info("绑定消息key规则:{}", routingKey);
		return BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
	}

	@Bean("eazybuilderRabbitTemplate")
	public RabbitTemplate rabbitTemplate() {
//		cachingConnectionFactory.setPublisherConfirmType(ConfirmType.SIMPLE);
		RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
		rabbitTemplate.setMessageConverter(convert());

		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			Method mh = null;
			Object callback = SpringContextUtil.getBean(confirmCallbackBean);
			if (null != callback) {

				mh = ReflectionUtils.findMethod(callback.getClass(), "excute",
						new Class[] { CorrelationData.class, boolean.class, String.class });
			}
			if (null == mh) {
				logger.warn("使用默认的rabbitmq confirm callback");
				callback = SpringContextUtil.getBean("defaultRabbitmqConfirmCallbackService");
				mh = ReflectionUtils.findMethod(callback.getClass(), "excute",
						new Class[] { CorrelationData.class, boolean.class, String.class });
			}

			ReflectionUtils.invokeMethod(mh, callback, new Object[] { correlationData, ack, cause });

		});

		// 消息是否正确有exchange路由到对应的消息队列queue,失败了，则调用此回调函数
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {

			Method mh = null;
			Object callback = SpringContextUtil.getBean(returnCallbackBean);
			if (null != callback) {
				mh = ReflectionUtils.findMethod(callback.getClass(), "excute",
						new Class[] { Message.class, int.class, String.class, String.class, String.class });
			}
			if (null == mh) {

				logger.warn("使用默认的rabbitmq return callback");
				callback = SpringContextUtil.getBean("defaultRabbitmqReturnCallbackService");
				mh = ReflectionUtils.findMethod(callback.getClass(), "excute",
						new Class[] { Message.class, int.class, String.class, String.class, String.class });
			}

			ReflectionUtils.invokeMethod(mh, callback,
					new Object[] { message, replyCode, replyText, exchange, routingKey });

		});

		return rabbitTemplate;

	}

	@Bean
	public Jackson2JsonMessageConverter convert() {
		return new Jackson2JsonMessageConverter();
	}

}
