package com.eazybuilder.ci.auth;

import com.eazybuilder.ci.OperLog;
import com.eazybuilder.ci.auth.ldap.LdapService;
import com.eazybuilder.ci.auth.ldap.LdapUser;
import com.eazybuilder.ci.config.LoadConfigYML;
import com.eazybuilder.ci.constant.RoleEnum;
import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.service.SystemPropertyService;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.eazybuilder.ci.service.UserService;
import com.eazybuilder.ci.util.AuthUtils;
import com.eazybuilder.ci.util.HttpUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/login")
public class LoginController {
	private static final String USER_ADMIN = "admin";
	private static Logger logger=LoggerFactory.getLogger(LoginController.class);
	private static Properties properties = new LoadConfigYML().getConfigProperties();
	@Autowired
	AccessTokenService accessService;

	@Autowired
	UserService userService;

	@Autowired
	TeamServiceImpl teamServiceImpl;

	@Autowired(required = false)
	LdapService ldapService;

	@Autowired
	SystemPropertyService propService;

	@Value("${admin.passwd:admin}")
	String passwd;

	@Value("${portal.used:false}")
	private Boolean used;

	@PostConstruct
	public void initSuper(){
		User user=userService.findByEmail(USER_ADMIN);
		if(user!=null){return;}
		user=new User();
		user.setRolesByRoleEnum(RoleEnum.admin);
		user.setEmail(USER_ADMIN);
		user.setPassword(USER_ADMIN);
		user.setName("平台管理员");
		userService.save(user,true);
	}

	@RequestMapping(method=RequestMethod.POST)
	@OperLog(module = "auth",opType = "login",opDesc = "登录")
	public Map login(@RequestBody LoginDTO dto,HttpServletRequest request) throws Exception{
		if(ldapService!=null&&!adminLogin(dto)){
			//login by ldap
			if(!dto.getLoginName().contains("@")) {
				dto.setLoginName(dto.getLoginName()+ properties.getProperty("email.suffix"));
			}
			try {
				return authByLdap(dto);
			} catch (Exception e) {
				if("true".equals(propService.getValue("login.local.enable", "false"))){
					//auth by ldap failed fallback to local login
					logger.warn("ldap auth failed,fall back to local login");
				}else {
					logger.error("",e);
					throw new AuthorizeFailedException();
				}
			}
		}

		return authByLocal(dto, request);

	}

	private Map authByLocal(LoginDTO dto, HttpServletRequest request) throws Exception {
		if(!USER_ADMIN.equals(dto.getLoginName())&&!dto.getLoginName().contains("@")){
			throw new AuthorizeFailedException();
		}
		User user=userService.findByEmail(dto.getLoginName());
		String code=AuthUtils.encrypt(dto.getPasswd(), dto.getLoginName());
		if(user==null||
				!user.getPassword().equals(code)){
			throw new AuthorizeFailedException();
		}

		logger.info("USER {} LOGIN, REMOTE IP:{}",dto.getLoginName(),HttpUtil.getClientIpAddress(request));
		Map tokenMap=Maps.newHashMap();
		tokenMap.put("access_token", accessService.createToken(user));
		tokenMap.put("user",user);
		//用户是否被指定为某个项目的配置管理员
		tokenMap.put("isCM", teamServiceImpl.isTeamConfigManager(user));
		AuthUtils.ACCESS_USER.set(UserVo.Instance(user));
		return tokenMap;
	}

	private boolean adminLogin(LoginDTO dto) {
		return dto.getLoginName().equals(USER_ADMIN);
	}

	private Map authByLdap(LoginDTO dto) throws Exception {
		User user = new User();
		if(used) {
			//如果设置了ldap服务地址，尝试通过ldap认证
			if (!ldapService.authenticate(dto.getLoginName().substring(0, dto.getLoginName().indexOf("@")),
					dto.getPasswd())) {
				throw new AuthorizeFailedException();
			}

			//从LDAP同步用户信息
			LdapUser ldapUser = ldapService.getUserInfo(dto.getLoginName());
			user.setDepartment(ldapUser.getDepartment());
			user.setEmail(dto.getLoginName());
			user.setName(ldapUser.getCn());
			user.setPassword(AuthUtils.encrypt(dto.getPasswd(), dto.getLoginName()));
			user.setPhone(ldapUser.getMobile());
			user.setTitle(ldapUser.getTitle());
			user.setRoles(user.getRoleByTitle());
		}
		User existUser=userService.findByEmail(dto.getLoginName());
		if(used) {
			user.setRoles(existUser.getRoles() != null ? existUser.getRoles() : user.getRoleByTitle());
		}
		if(existUser!=null) {
			//update user info by ldap
			if(used) {
				BeanUtils.copyProperties(user, existUser, "id","role");
				userService.save(existUser);
			}
			Map tokenMap=Maps.newHashMap();
			tokenMap.put("access_token", accessService.createToken(existUser));
			tokenMap.put("user",existUser);
			//用户是否被指定为某个项目的配置管理员
			tokenMap.put("isCM", teamServiceImpl.isTeamConfigManager(existUser));
			AuthUtils.ACCESS_USER.set(UserVo.Instance(existUser));
			return tokenMap;
		}else {
			//insert user info
			userService.save(user);
			Map tokenMap=Maps.newHashMap();
			tokenMap.put("access_token", accessService.createToken(user));
			tokenMap.put("user",user);
			AuthUtils.ACCESS_USER.set(UserVo.Instance(existUser));
			return tokenMap;
		}
	}

	public static class LoginDTO{
		private String loginName;
		private String passwd;

		public String getLoginName() {
			return loginName;
		}
		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}
		public String getPasswd() {
			return passwd;
		}
		public void setPasswd(String passwd) {
			this.passwd = passwd;
		}
	}

	@ResponseStatus(code=HttpStatus.FORBIDDEN,reason="认证失败,请重试")
	public static class AuthorizeFailedException extends RuntimeException{
		public AuthorizeFailedException() {
			super();
		}

		public AuthorizeFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
			super(message, cause, enableSuppression, writableStackTrace);
		}

		public AuthorizeFailedException(String message, Throwable cause) {
			super(message, cause);
		}

		public AuthorizeFailedException(String message) {
			super(message);
		}

		public AuthorizeFailedException(Throwable cause) {
			super(cause);
		}

	}
}
