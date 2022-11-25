package com.eazybuilder.dm;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.jmx.support.RegistrationPolicy;

import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;

@SpringBootApplication
@EnableDiscoveryClient
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@NacosConfigurationProperties(dataId="eazybuilder-notify",prefix="yml",groupId="dev",autoRefreshed=true)
public class NotifyApplication {
	private static Logger logger = LoggerFactory.getLogger(NotifyApplication.class);

    public static void main(String[] args) throws UnknownHostException {
    	ConfigurableApplicationContext application = SpringApplication.run(NotifyApplication.class, args);

		Environment env = application.getEnvironment();
		 logger.warn("\n----------------------------------------------------------\n\t" +
				 "应用 '{}' 运行成功! 访问连接:\n\t" +
				 "Swagger文档: \t\thttp://{}:{}{}/doc.html\n" +
				 "----------------------------------------------------------",
				 env.getProperty("spring.application.name"),
				 InetAddress.getLocalHost().getHostAddress(),
				 env.getProperty("server.port"),
				 env.getProperty("server.servlet.context-path"));
    }
    

}