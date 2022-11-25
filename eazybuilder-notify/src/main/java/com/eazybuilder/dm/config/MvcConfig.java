package com.eazybuilder.dm.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.redeazybuilderon.Redeazybuilderon;
import org.redeazybuilderon.api.RedeazybuilderonClient;
import org.redeazybuilderon.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {


	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(RestTemplateBuilder builder){
		return builder.build();
	}


	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*").allowCredentials(true).allowedHeaders("*").allowedMethods("*");
	}


	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public ObjectMapper jacksonObjectMapper() {
		ObjectMapper mapper=new ObjectMapper();
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.registerModule(new Hibernate5Module());//json序列化时不加载lazy属性
		return mapper;
	}

	@Bean
	public SerializationConfig serializationConfig() {
		return jacksonObjectMapper().getSerializationConfig();
	}

}