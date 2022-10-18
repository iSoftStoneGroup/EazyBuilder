package com.eazybuilder.ci;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;



import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@Configuration
@EnableSwagger
public class MvcConfig extends WebMvcConfigurerAdapter {
	@Autowired
	SpringSwaggerConfig  swaggerConfig;

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
	public SwaggerSpringMvcPlugin customSwaggerPlugin(){
		return new SwaggerSpringMvcPlugin(this.swaggerConfig)
				.apiInfo(apiInfo()).includePatterns(
						"/api/.*");
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource){
		return new JdbcTemplate(dataSource);
	}


	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"Eazybuilder持续集成系统REST API接口",
				"",
				"©2002-"+LocalDate.now().getYear()+" 软通动力",
				"",
				"",
				"");
		return apiInfo;
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

	@Bean
	public RedissonClient  redissonClient(@Value("${ci.redis.address:redis-cluster-0.redis-cluster.dev.svc.cluster.local:6379,redis-cluster-1.redis-cluster.dev.svc.cluster.local:6379,redis-cluster-2.redis-cluster.dev.svc.cluster.local:6379}")String redisAddress) {
		Config config=new Config();
		String[] split = redisAddress.split(",");
		if(split.length==1){
			//单节点
			config.useSingleServer().setAddress(redisAddress);
		}else {
			//指定使用集群部署方式
			config.useClusterServers()
					// 集群状态扫描间隔时间，单位是毫秒
					.setScanInterval(2000)
					//cluster方式至少6个节点(3主3从，3主做sharding，3从用来保证主宕机后可以高可用)
					.addNodeAddress("redis://" + split[0])
					.addNodeAddress("redis://" + split[1])
					.addNodeAddress("redis://" + split[2]);
		}
		return Redisson.create(config);
	}
}