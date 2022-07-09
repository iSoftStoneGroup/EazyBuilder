package com.eazybuilder.ci;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class UpmsConfig {

	// upms token
	@Value("${upms.gateway.token}")
	public String token;

	// 声明交换机
	@Bean("upmsHeader")
	public HttpHeaders upmsHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", token);
		headers.set("Grant-Type", "api_token");

		return headers;
	}
}
