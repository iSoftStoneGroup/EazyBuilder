package com.eazybuilder.ci.auth;



import com.eazybuilder.ci.util.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ConditionalOnProperty(prefix = "portal", value = "used",havingValue = "true")
@WebFilter(filterName="accessFilter",urlPatterns="/api/*")
public class UpmsAccessFilter implements Filter{

	@Autowired
	AccessTokenService service;


	

	@Override
	public void init(FilterConfig filterConfig)  {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token=request.getParameter("token");
		HttpServletResponse resp=(HttpServletResponse)response;
		HttpServletRequest httprequest = (HttpServletRequest)request;
		System.out.println(httprequest.getRequestURL());
		AuthUtils.ACCESS_USER.set(service.getUser(token));
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}
}
