package com.eazybuilder.ga.controller;


import com.alibaba.nacos.api.utils.StringUtils;
import com.eazybuilder.ga.pojo.Result;
import com.eazybuilder.ga.service.SendService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送通知请求
 */

@RestController
@RequestMapping("/send")
public class SendController {
	private static Logger logger = LoggerFactory.getLogger(SendController.class);

	@Autowired
	private SendService sendService;

	@ApiOperation(value = "发送消息给钉钉和邮件")
	@PostMapping("/message")
	public Result message(String message) {
		//1.校验参数
		if (StringUtils.isBlank(message)){
			return Result.no().message("输入为空");
		}
		logger.info("gitlab webhook");
		System.out.println(message);
		//2.调用方法返回结果
		String sendMessageResult = sendService.messageSend(message);
		//3.结果处理
		//4.结果判断
		if ("成功".equals(sendMessageResult)){
			return Result.ok();
		}else{
			return Result.no().message(sendMessageResult);
		}
	}
}
