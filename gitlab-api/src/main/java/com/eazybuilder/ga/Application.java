package com.eazybuilder.ga;



import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages={"com.eazybuilder.ga","com.eazybuilder.dm"})
public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws UnknownHostException {
    	ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);

		Environment env = application.getEnvironment();
		 logger.warn("\n----------------------------------------------------------\n\t" +
				 "应用 '{}' 运行成功!访问连接:\n\t" +
				 "Swagger文档: \t\thttp://{}:{}{}/doc.html\n" +
				 "----------------------------------------------------------",
				 env.getProperty("spring.application.name"),
				 InetAddress.getLocalHost().getHostAddress(),
				 env.getProperty("server.port"),
				 env.getProperty("server.servlet.context-path"));
    }


}
