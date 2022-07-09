package com.eazybuilder.ci.auth;

import java.util.UUID;

import javax.annotation.PostConstruct;

import com.alibaba.fastjson.JSON;
import com.eazybuilder.ci.controller.vo.UserVo;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eazybuilder.ci.entity.User;

/**
 * token验证
 *
 */
@Component
public class AccessTokenService {
	Logger logger= LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RedissonClient client;
	
	//全局凭证信息
	RMap<String,Long> tokenStorage;
	
	RMap<String,String> tokenMap;
	
	@PostConstruct
	public void init() {
		tokenStorage=client.getMap("CI-TOKEN-STORAGE");
		tokenMap=client.getMap("CI-TOKEN-MAP");
	}
	
	public String createToken(User user){
		String token=UUID.randomUUID().toString();
		tokenStorage.put(token, System.currentTimeMillis());
		tokenMap.put(token, JSON.toJSONString(user));
		return token;
	}
	
	public boolean isValidToken(String token){
		Long lastAccess=tokenStorage.get(token);
		long currentTime=System.currentTimeMillis();
		
		if(lastAccess==null||currentTime-lastAccess>30*60*1000L){
			//remove invalid token
			tokenStorage.remove(token);
			return false;
		}
		tokenStorage.put(token, currentTime);
		return true;
		
	}
	
	public UserVo getUser(String token){
		String s = tokenMap.get(token);
		return JSON.parseObject(s, UserVo.class);
	}


	public String upmsSaveToken(User user,String token){
		tokenStorage.put(token, System.currentTimeMillis());
		tokenMap.put(token, JSON.toJSONString(user));
		return token;
	}
}
