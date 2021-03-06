package com.eazybuilder.ci;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.time.LocalDate;

@Configuration
@EnableSwagger
public class MvcConfig extends WebMvcConfigurerAdapter {
	@Autowired
	SpringSwaggerConfig  swaggerConfig;

	@Bean
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
				"eazybuilder??????????????????REST API??????",
				"",
				"??2002-"+LocalDate.now().getYear()+" ????????????",
				"",
				"",
				"");
		return apiInfo;
	}

	@Bean
	public ObjectMapper jacksonObjectMapper() {
		ObjectMapper mapper=new ObjectMapper();
		// ????????????????????????JSON?????????????????????Java???????????????????????????
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.registerModule(new Hibernate5Module());//json?????????????????????lazy??????
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
			//?????????
			config.useSingleServer().setAddress(redisAddress);
		}else {
			//??????????????????????????????
			config.useClusterServers()
					// ????????????????????????????????????????????????
					.setScanInterval(2000)
					//cluster????????????6?????????(3???3??????3??????sharding???3??????????????????????????????????????????)
					.addNodeAddress("redis://" + split[0])
					.addNodeAddress("redis://" + split[1])
					.addNodeAddress("redis://" + split[2]);
		}
		return Redisson.create(config);
	}
}