package com.eazybuilder.ci.controller;//package com.eazybuilder.ci.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.eazybuilder.ci.entity.Project;
//import com.eazybuilder.ci.util.JsonMapper;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.support.CorrelationData;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.io.UnsupportedEncodingException;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/rabbitmq/message")
//public class SendMsgController {
//	private static Logger logger = LoggerFactory.getLogger(SendMsgController.class);
//
//	// 注入RabbitMQ的模板
//	@Resource
//	@Qualifier(value = "eazybuilderRabbitTemplate")
//	private RabbitTemplate rabbitTemplate;
//	// 交换机名称
//	@Value("${message.topicExchange}")
//	public String ITEM_TOPIC_EXCHANGE;
//
//	/**
//	 * 测试
//	 */
//	@GetMapping("/sendmsg")
//	public String sendMsg(@RequestParam String rabbitMqExchange, @RequestParam String key,@RequestParam String msg) throws UnsupportedEncodingException {
//
//
//
//		 key = "redmine.addNote";
//		 msg ="{\"data\":{\n" +
//				 "  \"flag\": \"1\",\n" +
//				 "  \"message\": \"步骤:checkout from scm 执行耗时: 0秒 执行状态：SUCCESS\\n步骤:update ci-tools 执行耗时: 1秒 执行状态：SUCCESS\\n步骤:maven build 执行耗时: 30秒 执行状态：SUCCESS\\n步骤:decorate project 执行耗时: 2秒 执行状态：SUCCESS\\n步骤:k8s deploy 执行耗时: 0秒 执行状态：SUCCESS\\n步骤:ansible deploy 执行耗时: 0秒 执行状态：SUCCESS\\n步骤:Declarative: Post Actions 执行耗时: 0秒 执行状态：SUCCESS\\n\\nk8s自动部署结果：成功\\nci详细日志：http://ci-ingress.eazybuilder-devops.cn//ci/resources/ebf83a12-640f-4a65-85a0-8eb054e836d9\\n详细日志：http://ci-ingress.eazybuilder-devops.cn//ci/resources/ebf83a12-640f-4a65-85a0-8eb054e836d9\",\n" +
//				 "  \"userName\": \"qiujin\",\n" +
//				 "  \"code\": \"1,2,3,4,6,7,8\"\n" +
//				 "}}";
//		UUID correlationDataId = UUID.randomUUID();
//		logger.info("发送消息，msgId:{}", correlationDataId);
//		CorrelationData correlationData = new CorrelationData(correlationDataId.toString());
//		MessageProperties messageProperties = new MessageProperties();
//		messageProperties.setContentType("text/x-json");
//		Object parse = JSONObject.parse(msg);
////		rabbitTemplate.convertAndSend(rabbitMqExchange, key, net.sf.json.JSONObject.fromObject(msg), correlationData);
////		rabbitTemplate.convertAndSend(rabbitMqExchange, key, JSONObject.parse(msg), correlationData);
//		Message message = new Message(msg.getBytes("UTF-8"), messageProperties);
//		String  sendStr = JsonMapper.nonDefaultMapper().toJson(msg);
////			rabbitTemplate.convertAndSend(ITEM_TOPIC_EXCHANGE, key, message, correlationData);
////		rabbitTemplate.convertAndSend(rabbitMqExchange, key, parse.toString(), correlationData);
////			JSONObject json=new JSONObject.
////		rabbitTemplate.convertAndSend(rabbitMqExchange, key, sendStr, correlationData);
//
//		rabbitTemplate.convertAndSend(ITEM_TOPIC_EXCHANGE, key, message, correlationData);
//
////		UUID correlationDataId = UUID.randomUUID();
////		logger.info("发送消息，msgId:{}", correlationDataId);
////		CorrelationData correlationData = new CorrelationData(correlationDataId.toString());
//////		msg =com.alibaba.fastjson.JSONObject.toJSONString(project);
////		rabbitTemplate.convertAndSend(rabbitMqExchange, key, msg, correlationData);
//		// 返回消息
//		return "success send message to " + rabbitMqExchange;
//	}
//}
