package com.eazybuilder.ci.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eazybuilder.ci.controller.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eazybuilder.ci.entity.ApiSecurity;
import com.eazybuilder.ci.entity.Team;
import com.eazybuilder.ci.entity.User;
import com.eazybuilder.ci.service.ApiSecurityService;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.eazybuilder.ci.service.UserService;
import com.eazybuilder.ci.util.AuthUtils;


@WebFilter(filterName="apiAuthFilter",urlPatterns="/open-api/*")
@Component
public class ApiAuthFilter implements Filter{
	private static Logger logger=LoggerFactory.getLogger(ApiAuthFilter.class);

	@Autowired
	AccessTokenService service;
	
	@Autowired
	ApiSecurityService apiSecService;
	
	
	@Autowired
	UserService  userService;
	
	@Autowired
    TeamServiceImpl teamServiceImpl;

	@Value("${portal.used:false}")
	private Boolean used;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp=(HttpServletResponse)response;
		User accessUser=null;
		boolean authed=false;
		if(request instanceof HttpServletRequest) {
			String apiToken=((HttpServletRequest)request).getHeader("CI-TOKEN");
			if(apiToken!=null) {
				logger.info("API ACCESS: {}",apiToken);
				ApiSecurity checkResult=apiSecService.check(apiToken);
				if(checkResult!=null&&checkResult.getUserId()!=null) {
					accessUser=userService.findOne(checkResult.getUserId());
					Team team= teamServiceImpl.findOne(checkResult.getTeamId());
					if(accessUser!=null&&team!=null) {
						logger.info("User:{} Access {} by Direct API Request,Default Team:{}",accessUser.getEmail(),((HttpServletRequest) request).getRequestURI(),team.getName());
						authed=true;
						AuthUtils.DEFAULT_TEAM.set(team);
					}else {
						logger.info("USER {} or Team {} is null",checkResult.getUserId(),checkResult.getTeamId());
					}
				}else {
					logger.info("TOKEN CHECK FAILED: {}",checkResult);
				}
			}
		}
		if(!authed||accessUser==null) {
			resp.setStatus(403);
			resp.getWriter().write("invalid api token");
			return;
		}
		AuthUtils.ACCESS_USER.set(UserVo.Instance(accessUser));
		try {
			chain.doFilter(request, response);
		} finally {
			//clear up thread local
			AuthUtils.ACCESS_USER.remove();
			AuthUtils.DEFAULT_TEAM.remove();
		}
	}

	@Override
	public void destroy() {
		
	}
}
