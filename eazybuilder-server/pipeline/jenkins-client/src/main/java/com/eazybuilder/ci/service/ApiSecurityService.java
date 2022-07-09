package com.eazybuilder.ci.service;

import org.springframework.stereotype.Service;

import com.eazybuilder.ci.base.AbstractCommonServiceImpl;
import com.eazybuilder.ci.base.CommonService;
import com.eazybuilder.ci.entity.ApiSecurity;
import com.eazybuilder.ci.entity.QApiSecurity;
import com.eazybuilder.ci.repository.ApiSecurityDao;
import com.eazybuilder.ci.util.ApiTokenUtil;
import com.eazybuilder.ci.util.ApiTokenUtil.ApiToken;

@Service
public class ApiSecurityService extends AbstractCommonServiceImpl<ApiSecurityDao, ApiSecurity> 
		implements CommonService<ApiSecurity>{

	public ApiSecurity check(String token) {
		ApiToken apiToken=ApiTokenUtil.parseToken(token);
		
		String[] resources=apiToken.getResource().split(";");
		String teamId=resources[0];
		String userId=resources[1];
		
		ApiSecurity record=dao.findOne(QApiSecurity.apiSecurity.secret.eq(apiToken.getSecret())
				.and(QApiSecurity.apiSecurity.teamId.eq(teamId))
				.and(QApiSecurity.apiSecurity.userId.eq(userId))).get();
		return record;
		
	}
	
	public String generateApiToken(ApiSecurity data) throws Exception {
		ApiToken apiToken=new ApiToken();
		apiToken.setExpired(-1);
		apiToken.setResource(data.getTeamId()+";"+data.getUserId());
		apiToken.setSecret(data.getSecret());
		return ApiTokenUtil.generateToken(apiToken);
	}
}
