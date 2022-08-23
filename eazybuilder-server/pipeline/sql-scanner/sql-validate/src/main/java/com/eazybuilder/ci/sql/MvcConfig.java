package com.eazybuilder.ci.sql;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@Configuration
@EnableSwagger
public class MvcConfig extends WebMvcConfigurerAdapter {
	@Autowired
	SpringSwaggerConfig  swaggerConfig;
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }
    
    @Override
	 public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**")
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
                "Eazybuilder持续集成系统-SQL扫描模块REST API接口",
                "",
                "",
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
		 mapper.registerModule(new Hibernate4Module());//json序列化时不加载lazy属性
	     return mapper;
	 }
	 
	 @Bean
	 public SerializationConfig serializationConfig() {
		 return jacksonObjectMapper().getSerializationConfig();
	 }
}