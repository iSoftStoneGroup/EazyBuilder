package com.eazybuilder.ci.auth;

import com.eazybuilder.ci.controller.vo.UserVo;
import com.eazybuilder.ci.service.TeamServiceImpl;
import com.eazybuilder.ci.service.UserService;
import com.eazybuilder.ci.util.AuthUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@ConditionalOnProperty(prefix = "portal", value = "used",havingValue = "false")
@WebFilter(filterName="accessFilter",urlPatterns="/api/*")
@Component
public class AccessFilter implements Filter{
	private static Logger logger=LoggerFactory.getLogger(AccessFilter.class);

	@Autowired
	AccessTokenService service;
	
	@Autowired
	UserService  userService;
	
	@Autowired
    TeamServiceImpl teamServiceImpl;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httprequest = (HttpServletRequest)request;
		System.out.println(httprequest.getRequestURL());
		String token=request.getParameter("token");
		HttpServletResponse resp=(HttpServletResponse)response;
		UserVo accessUser=null;
		boolean authed=false;
		if(!StringUtils.isBlank(token)) {
			accessUser=service.getUser(token);
			if(accessUser!=null&&service.isValidToken(token)) {
				authed=true;
			}
		}
		if(!authed) {
			resp.setStatus(403);
			resp.getWriter().write("invalid access token");
			return;
		}
		AuthUtils.ACCESS_USER.set(accessUser);
		try {
			chain.doFilter(request, response);
		} finally {
			//clear up thread local
			AuthUtils.ACCESS_USER.remove();
		}
	}

	@Override
	public void destroy() {
		
	}
}
