package com.eazybuilder.ci.util;

import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.Role;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.User;
import com.jcraft.jsch.jce.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.List;

public class AuthUtils {
	private static Logger logger = LoggerFactory.getLogger(AuthUtils.class);
	public static ThreadLocal<UserVo> ACCESS_USER=new ThreadLocal<>();
	
	public static ThreadLocal<Team> DEFAULT_TEAM=new ThreadLocal<>();
	
	public static UserVo getCurrentUser(){
		UserVo user = ACCESS_USER.get();
		if(user!=null) {
			logger.info("获取当前登录用户信息：{}", user.toString());
		}else{
			logger.error("当前登录用户信息为空");
		}
		return user;
	}


	public static Team getDefaultTeam() {
		return DEFAULT_TEAM.get();
	}
	
	public static String encrypt(String passcode,String salt) throws Exception{
		SHA256 sha=new SHA256();
		sha.init();
		byte[] content=(passcode+salt).getBytes("utf-8");
		sha.update(content, 0, content.length-1);
		return Base64.getEncoder().encodeToString(sha.digest());
	}


	public static boolean isAuditOrAdmin(){
		User currentUser=AuthUtils.getCurrentUser();
		List<Role> roles = currentUser.getRoles();
		if(Role.existRole(roles, RoleEnum.admin) || Role.existRole(roles,RoleEnum.audit)) {
			return true;
		}
		return false;
	}
	
}
