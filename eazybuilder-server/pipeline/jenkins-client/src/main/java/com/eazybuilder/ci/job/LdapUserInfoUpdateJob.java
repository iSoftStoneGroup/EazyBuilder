package com.eazybuilder.ci.job;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.service.SystemPropertyService;
import com.eazybuilder.ci.service.UserService;
import com.eazybuilder.ci.util.HttpUtil;

@Component
public class LdapUserInfoUpdateJob {
	
	private static Logger logger=LoggerFactory.getLogger(LdapUserInfoUpdateJob.class);

	@Autowired
	UserService userService;
	
	@Autowired
	SystemPropertyService propService;
	
	//每个月更新一次
//	@Scheduled(cron="1 * * * * ? ")
	@Scheduled(cron="0 1 0 1 * ? ")
	public void updateUserInfo(){
		String ldapServiceUrl=propService.getValue("ldap.service.url");
		logger.info("start update user info with ldap");
		if(StringUtils.isNotBlank(ldapServiceUrl)){
			Iterable<User> users=userService.findAll();
			if(users!=null){
				for(User user:users){
					if("admin".equals(user.getEmail())){
						continue;
					}
					try {
						String ldapUserInfo=HttpUtil.getJson(ldapServiceUrl, "mail="+user.getEmail());
						JSONObject ldapUser=JSONObject.parseObject(ldapUserInfo);
						user.setDepartment(ldapUser.getString("department"));
						user.setEmail(ldapUser.getString("userPrincipalName"));
						user.setName(ldapUser.getString("cn"));
						user.setPhone(ldapUser.getString("mobile"));
						user.setTitle(ldapUser.getString("title"));
						user.setRoles(user.getRoleByTitle());
						userService.save(user);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			
		}
	}
}
