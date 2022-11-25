package com.eazybuilder.dm.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RibbonConfiguration {

	@Bean
	@LoadBalanced
	public RestTemplate rebbionRestTemplate() {
		return new RestTemplate();
	}

	@Bean(name = "remoteRestTemplate")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean(name = "remoteHttpsRestTemplate")
	public RestTemplate geTemplate() {
		return new RestTemplate(new HttpsClientRequestFactory());
	}

}
