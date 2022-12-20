package com.eazybuilder.ga.controller;


import com.alibaba.nacos.api.utils.StringUtils;
 import com.eazybuilder.ga.pojo.PreReceivePojo;
import com.eazybuilder.ga.pojo.Result;
import com.eazybuilder.ga.service.CheckService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 检查代码
 */
@RestController
@RequestMapping("/check")
@CrossOrigin
public class CheckController {
	 private static Logger logger = LoggerFactory.getLogger(CheckController.class);

	 @Autowired
	 private CheckService checkService;


	@ApiOperation(value = "校验需求号")
	@PostMapping("/number")
 	public String number(@RequestBody PreReceivePojo data) {

		String userName = data.getUserName();
		String notes = data.getNotes();

		//1.校验参数
		if (StringUtils.isBlank(userName)||StringUtils.isBlank(notes)){
			return Result.no().message("输入参数不可为空").toString();
		}
		//2.调用方法返回结果
		String checkDemandNumberResult = checkService.numberCheck(notes,userName);
		//3.结果判断
		if ("成功".equals(checkDemandNumberResult)){
			return Result.ok().toString();
		}else{
			return Result.no().message(checkDemandNumberResult).toString();
		}
	}

	@ApiOperation(value = "校验需求号测试")
	@GetMapping("/test")
	public Result test() {
		//logger.info("gitlab callback ,check comments:{},branch:{},userName:{}",code,password,userName);
		String node ="xxx";
		String[] arr = node.split(" ");
		System.out.println(arr);
		System.out.println(arr[5]);
		String checkDemandNumberResult = checkService.numberCheck("[ADD]FEATURE-47 新增xx管理功能", arr[5]);

		return Result.ok().message(checkDemandNumberResult);
	}

}
