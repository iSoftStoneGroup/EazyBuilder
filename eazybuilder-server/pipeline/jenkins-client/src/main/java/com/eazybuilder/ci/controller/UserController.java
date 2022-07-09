package com.eazybuilder.ci.controller;

import java.util.List;

import com.eazybuilder.ci.constant.RoleEnum;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eazybuilder.ci.OperLog;
import com.eazybuilder.ci.base.CRUDRestController;
import com.eazybuilder.ci.entity.Role;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.service.UserService;
import com.eazybuilder.ci.util.AuthUtils;
import com.wordnik.swagger.annotations.ApiOperation;
@RestController
@RequestMapping("/api/user")
public class UserController extends CRUDRestController<UserService, User>{

	@RequestMapping(value="/all",method={RequestMethod.POST,RequestMethod.PUT})
	@ApiOperation("保存全部")
	@OperLog(module = "user",opType = "saveWithPwd",opDesc = "保存用户")
	public User saveWithPwd(@RequestBody User entity){
		service.save(entity,true);
		return entity;
	}
	
	@RequestMapping(value="/reset",method={RequestMethod.POST,RequestMethod.PUT})
	@OperLog(module = "user",opType = "resetPwd",opDesc = "重置密码")
	public void resetPassword(@RequestBody List<String>  ids) throws Exception{
		//check PRIVILEDGE (admin required)
		if(AuthUtils.getCurrentUser()==null|| !Role.existRole(AuthUtils.getCurrentUser().getRoles(),RoleEnum.admin)){
			throw new IllegalAccessException("您没有权限重置用户信息");
		}
		for(String id:ids){
			//reset password to default
			User user=service.findOne(id);
			user.setPassword(AuthUtils.encrypt(UserService.DEFAULT_TOKEN, user.getEmail()));
			service.save(user);
		}
	}
}
