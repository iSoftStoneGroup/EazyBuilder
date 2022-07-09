package com.eazybuilder.ci;

import com.eazybuilder.ci.storage.ResourceStorageService;
import com.eazybuilder.ci.storage.impl.LocalFileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ResourceStorageAutoConfiguration {
	private static Logger logger=LoggerFactory.getLogger(ResourceStorageAutoConfiguration.class);

	@Primary
	//@ConditionalOnProperty(name = "ci.storage.type",havingValue = "local",matchIfMissing = true)
	@Bean
	public ResourceStorageService localStorage() {
		logger.info("USE LOCAL STORAGE SERVICE");
		return new LocalFileStorageService();
	}
}
