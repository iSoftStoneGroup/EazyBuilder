package com.eazybuilder.ga.controller;


import com.alibaba.nacos.api.utils.StringUtils;
import com.eazybuilder.ga.pojo.GitLabPojo;
import com.eazybuilder.ga.pojo.Result;
import com.eazybuilder.ga.service.SendService;
import com.eazybuilder.ga.service.SettingService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设置gitlab
 */

@RestController
@RequestMapping("/setting")
public class SettingController {
	 private static Logger logger = LoggerFactory.getLogger(SettingController.class);

	 @Autowired
	 private SettingService settingService;


	@ApiOperation(value = "创建群组")
	@PostMapping("/gitLab")
	public Result gitLab(@RequestBody GitLabPojo gitLabPojo) {
		logger.info("gitlab webhook");
		System.out.println(gitLabPojo);
		String result = settingService.gitLabSetting(gitLabPojo);
		if ("".equals(result)){
			return Result.ok();
		}else{
			return Result.no().message(result);
		}
	}

}
